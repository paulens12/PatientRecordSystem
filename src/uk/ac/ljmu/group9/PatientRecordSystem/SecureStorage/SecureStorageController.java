package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import uk.ac.ljmu.group9.PatientRecordSystem.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureStorageController implements IStorageController
{
    private static final String fileName = "D:\\PatientRecordSystem.db";
    private Database db;
    //private Cipher cipher;
    //private SecretKey key;
    private static final String keyPhrase = "fdjashr234h2qihbfjdksala";
    private FileOutputStream fileOut;
    private FileInputStream fileIn;
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$&*%^()\\-_=+{}\\[\\];'\\\\:\"|<>?,./])(?=.*[0-9])(?=.*[a-z]).{8,}$");
    //^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.\-_=+\[\]{};':",<>\\|])(?=.{8,})

    public SecureStorageController()
    {
        //final KeyGenerator kg = KeyGenerator.getInstance("AES");
        //kg.init(new SecureRandom());
        //this.key = kg.generateKey();
        //this.key = getKey(keyPhrase);
        //this.key = new SecretKeySpec(new byte[]{64,10,122,-105,79,-14,25,-90,-122,23,-87,-122,-72,-15,71,93}, "AES");
        //this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        this.db = new Database();
    }

    private SecretKeySpec getKey()
    {
        try
        {
            byte[] keyB = SecureStorageController.keyPhrase.getBytes("UTF-8");
            //System.out.println(keyB.length);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyB = sha.digest(keyB);
            keyB = Arrays.copyOf(keyB, 16); // use only first 128 bit
            //System.out.println(keyB.length);
            //System.out.println(new String(keyB, "UTF-8"));
            return new SecretKeySpec(keyB, "AES");

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String getUserKey(AccountType accType, String username)
    {
        return accType.GetText() + "-" + username;
    }

    public void Load()
    {
        File f = new File(fileName);
        if(f.isFile() && f.length() != 0)
        {
            try {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, getKey());
                FileInputStream fileIn = new FileInputStream(fileName);
                CipherInputStream inputStream = new CipherInputStream(fileIn, cipher);
                ObjectInputStream ois = new ObjectInputStream(inputStream);
                this.db = (Database) ois.readObject();
            } catch (InvalidKeyException | IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save()
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            FileOutputStream fileOut = new FileOutputStream(fileName);
            CipherOutputStream outputStream = new CipherOutputStream(fileOut, cipher);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(this.db);
            oos.flush();
            oos.close();
        } catch (InvalidKeyException | IOException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public boolean VerifyPassword(AccountType accType, String username, String password) throws IllegalArgumentException
    {
        String key = getUserKey(accType, username);
        return db.userCredentials.containsKey(key) && db.userCredentials.get(key).Verify(password);
    }

    public void ChangePassword(AccountType accType, String username, String newPassword) throws IllegalArgumentException
    {
        String key = getUserKey(accType, username);
        if(!db.userCredentials.containsKey(key))
            throw new IllegalArgumentException();
        db.userCredentials.get(key).SetPassword(newPassword);
    }

    public boolean VerifyPasswordStrength(String rawPassword)
    {
        Matcher matcher = passwordPattern.matcher(rawPassword);
        return matcher.matches();
    }

    private List<UUID> getTreatmentIds(AccountType accType, String username) throws IllegalArgumentException
    {
        switch(accType)
        {
            case Patient:
                if(!this.db.patients.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.patients.get(username).GetTreatments();
            case Doctor:
                if(!this.db.doctors.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.doctors.get(username).GetTreatments();
        }
        return null;
    }

    private List<UUID> getVisitIds(AccountType accType, String username) throws IllegalArgumentException
    {
        switch(accType)
        {
            case Patient:
                if(!this.db.patients.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.patients.get(username).GetVisits();
            case Doctor:
                if(!this.db.doctors.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.doctors.get(username).GetVisits();
        }
        return null;
    }

    public ArrayList<Treatment> GetTreatments(AccountType accType, String username) throws IllegalArgumentException
    {
        List<UUID> ids = getTreatmentIds(accType, username);
        ArrayList<Treatment> result = new ArrayList<>();
        if (ids != null)
            for(UUID id : ids)
                result.add(db.treatments.get(id));
        return result;
    }

    public ArrayList<Visit> GetPastVisits(AccountType accType, String username) throws IllegalArgumentException
    {
        List<UUID> ids = getVisitIds(accType, username);
        ArrayList<Visit> result = new ArrayList<>();
        if (ids != null)
            for(UUID id : ids)
            {
                Visit v = db.visits.get(id);
                if(v.Date.isBefore(LocalDateTime.now()))
                    result.add(v);
            }
        return result;
    }

    public ArrayList<Visit> GetFutureVisits(String username) throws IllegalArgumentException
    {
        List<UUID> ids = getVisitIds(AccountType.Doctor, username);
        ArrayList<Visit> result = new ArrayList<>();
        if (ids != null)
            for(UUID id : ids)
            {
                Visit v = this.db.visits.get(id);
                if(v.Date.isAfter(LocalDateTime.now()))
                    result.add(v);
            }
        return result;
    }

    public void AddVisit(LocalDateTime date, String patientUsername, String doctorUsername, Ailment ailment) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("patientUsername");
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException("doctorUsername");
        if(!this.db.doctors.get(doctorUsername).GetWorkingDay(date.getDayOfWeek().getValue()))
            throw new OutsideWorkingDaysException();
        Visit v = new Visit(date, ailment, patientUsername);
        UUID uuid = UUID.randomUUID();
        this.db.visits.put(uuid, v);
        this.db.patients.get(patientUsername).AddVisit(uuid);
        this.db.doctors.get(doctorUsername).AddVisit(uuid);
    }

    public void AddTreatment(LocalDateTime date, String patientUsername, String doctorUsername, TreatmentType treatment) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("patientUsername");
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException("doctorUsername");
        Treatment t = new Treatment(date, treatment, patientUsername, doctorUsername);
        UUID uuid = UUID.randomUUID();
        this.db.treatments.put(uuid, t);
        this.db.patients.get(patientUsername).AddTreatment(uuid);
        this.db.doctors.get(doctorUsername).AddTreatment(uuid);
    }

    public void SetPrivacy(String username, boolean accessGranted) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException("username");
        this.db.patients.get(username).PrivacySetting = accessGranted;
    }

    public boolean GetPrivacy(String username) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException("username");
        return this.db.patients.get(username).PrivacySetting;
    }

    public void AddDoctor(String username, String name, String address, boolean[] workingDays, String password)
    {
        Doctor d = new Doctor(name, address, workingDays);
        Credentials c = new Credentials(username, password);
        this.db.doctors.put(username, d);
        this.db.userCredentials.put("Doctor-" + username, c);
    }

    public void AddPatient(String username, String name, String address, String doctorUsername, String password) throws IllegalArgumentException
    {
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException();
        Patient p = new Patient(name, address, doctorUsername);
        Credentials c = new Credentials(username, password);
        this.db.patients.put(username, p);
        this.db.userCredentials.put("Patient-" + username, c);
        SetDoctor(username, doctorUsername);
    }

    public void RemoveDoctor(String username) throws IllegalArgumentException
    {
        if(!this.db.doctors.containsKey(username))
            throw new IllegalArgumentException();
        this.db.doctors.remove(username);
        this.db.userCredentials.remove("Doctor-" + username);
    }

    public void RemovePatient(String username) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException();
        this.db.patients.remove(username);
        this.db.userCredentials.remove("Patient-" + username);
    }

    private User getUser(AccountType accType, String username)
    {
        switch(accType)
        {
            case Patient:
                if(!this.db.patients.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.patients.get(username);
            case Doctor:
                if(!this.db.doctors.containsKey(username))
                    throw new IllegalArgumentException("username");
                return this.db.doctors.get(username);
        }
        return null;
    }

    public void ChangeName(AccountType accType, String username, String newName) throws IllegalArgumentException
    {
        User u = getUser(accType, username);
        if(u != null)
            u.name = newName;
    }

    public void ChangeAddress(AccountType accType, String username, String newAddress) throws IllegalArgumentException
    {
        User u = getUser(accType, username);
        if(u != null)
            u.address = newAddress;
    }

    public void SetDoctor(String patientUsername, String doctorUsername) throws IllegalArgumentException
    {
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException();
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException();
        this.db.patients.get(patientUsername).DoctorUsername = doctorUsername;
    }

    public void SetWorkingDays(String username, boolean[] workingDays) throws IllegalArgumentException
    {
        if(!this.db.doctors.containsKey(username))
            throw new IllegalArgumentException();
        this.db.doctors.get(username).SetWorkingDays(workingDays);
    }

    public String GetFirstChoiceDoctor(String patientUsername) throws IllegalArgumentException
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("username");
        return this.db.patients.get(patientUsername).DoctorUsername;
    }

    public String GetUserRealName(AccountType accType, String username) throws IllegalArgumentException
    {
        User u = getUser(accType, username);
        if(u != null)
            return u.name;
        return "";
    }

    public String GetUserAddress(AccountType accType, String username) throws IllegalArgumentException
    {
        User u = getUser(accType, username);
        if(u != null)
            return u.address;
        return "";
    }
}
