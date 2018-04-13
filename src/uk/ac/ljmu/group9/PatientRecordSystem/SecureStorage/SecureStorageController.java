package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import uk.ac.ljmu.group9.PatientRecordSystem.Ailment;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.TreatmentType;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureStorageController implements IStorageController
{
    private static final String fileName = "D:\\PatientRecordSystem.db";
    private Database db;
    private Cipher cipher;
    private SecretKey key;
    private FileOutputStream fileOut;
    private FileInputStream fileIn;
    private Pattern passwordPattern;

    public SecureStorageController()
    {
        try {
            this.fileOut = new FileOutputStream(fileName);
            this.fileIn = new FileInputStream(fileName);
            //final KeyGenerator kg = KeyGenerator.getInstance("AES");
            //kg.init(new SecureRandom());
            //this.key = kg.generateKey();
            this.key = new SecretKeySpec(new byte[]{64,10,122,-105,79,-14,25,-90,-122,23,-87,-122,-72,-15,71,93}, "AES");
            this.cipher = Cipher.getInstance("AES");
            this.passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.-_=+\\[\\]{};':\",<>\\\\|])(?=.{8,})");
            this.db = new Database();
            Load();
        } catch (FileNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
                this.cipher.init(Cipher.DECRYPT_MODE, this.key);
                CipherInputStream inputStream = new CipherInputStream(this.fileIn, this.cipher);
                ObjectInputStream ois = new ObjectInputStream(inputStream);
                this.db = (Database) ois.readObject();
            } catch (InvalidKeyException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save()
    {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
            CipherOutputStream outputStream = new CipherOutputStream(this.fileOut, this.cipher);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(this.db);
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean VerifyPassword(AccountType accType, String username, String password)
    {
        String key = getUserKey(accType, username);
        return db.userCredentials.containsKey(key) && db.userCredentials.get(key).Verify(password);
    }

    public void ChangePassword(AccountType accType, String username, String newPassword)
    {
        String key = getUserKey(accType, username);
        if(!db.userCredentials.containsKey(key))
            throw new IllegalArgumentException();
        db.userCredentials.get(key).SetPassword(newPassword);
    }

    public boolean VerifyPasswordStrength(String rawPassword)
    {
        Matcher matcher = this.passwordPattern.matcher(rawPassword);
        return matcher.matches();
    }

    private List<UUID> getTreatmentIds(AccountType accType, String username)
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

    public ArrayList<Treatment> GetTreatments(AccountType accType, String username)
    {
        List<UUID> ids = getTreatmentIds(accType, username);
        ArrayList<Treatment> result = new ArrayList<>();
        if (ids != null)
            for(UUID id : ids)
                result.add(db.treatments.get(id));
        return result;
    }

    public ArrayList<Visit> GetPastVisits(AccountType accType, String username)
    {
        List<UUID> ids = getTreatmentIds(accType, username);
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

    public ArrayList<Visit> GetFutureVisits(String username)
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException("username");
        List<UUID> ids = getTreatmentIds(AccountType.Doctor, username);
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

    public void AddVisit(LocalDateTime date, String patientUsername, String doctorUsername, Ailment ailment)
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("patientUsername");
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException("doctorUsername");
        Visit v = new Visit(date, ailment);
        UUID uuid = UUID.randomUUID();
        this.db.visits.put(uuid, v);
        this.db.patients.get(getUserKey(AccountType.Patient, patientUsername)).AddVisit(uuid);
        this.db.doctors.get(getUserKey(AccountType.Doctor, doctorUsername)).AddVisit(uuid);
    }

    public void AddTreatment(LocalDateTime date, String patientUsername, String doctorUsername, TreatmentType treatment)
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("patientUsername");
        if(!this.db.doctors.containsKey(doctorUsername))
            throw new IllegalArgumentException("doctorUsername");
        Treatment t = new Treatment(date, treatment, patientUsername, doctorUsername);
        UUID uuid = UUID.randomUUID();
        this.db.treatments.put(uuid, t);
        this.db.patients.get(getUserKey(AccountType.Patient, patientUsername)).AddTreatment(uuid);
        this.db.doctors.get(getUserKey(AccountType.Doctor, doctorUsername)).AddTreatment(uuid);
    }

    public void SetPrivacy(String username, boolean accessGranted)
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException("username");
        this.db.patients.get(username).PrivacySetting = accessGranted;
    }

    public boolean GetPrivacy(String username)
    {
        if(!this.db.patients.containsKey(username))
            throw new IllegalArgumentException("username");
        return this.db.patients.get(username).PrivacySetting;
    }

    public void AddDoctor(String username, String name, String address, boolean[] workingDays, String password)
    {

    }

    public void AddPatient(String username, String name, String address, String doctorUsername, String password)
    {

    }

    public void RemoveDoctor(String username)
    {

    }

    public void RemovePatient(String username)
    {

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

    public void ChangeName(AccountType accType, String username, String newName)
    {
        User u = getUser(accType, username);
        if(u != null)
            u.name = newName;
    }

    public void ChangeAddress(AccountType accType, String username, String newAddress)
    {
        User u = getUser(accType, username);
        if(u != null)
            u.address = newAddress;
    }

    public void SetDoctor(String patientUsername, String doctorUsername)
    {

    }

    public void SetWorkingDays(String username, boolean[] workingDays)
    {

    }

    public String GetFirstChoiceDoctor(String patientUsername)
    {
        if(!this.db.patients.containsKey(patientUsername))
            throw new IllegalArgumentException("username");
        return this.db.patients.get(patientUsername).DoctorUsername;
    }

    public void SetFirstChoiceDoctor(String patientUsername, String DoctorUsername)
    {

    }

    public String GetUserRealName(AccountType accType, String username)
    {
        if(!this.db.doctors.containsKey(username))
            throw new IllegalArgumentException("username");
        return this.db.doctors.get(username).name;
    }
}
