package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.util.HashMap;
import java.util.UUID;

public class Database implements java.io.Serializable
{
    protected HashMap<String, Credentials> userCredentials;
    protected HashMap<String, Patient> patients;
    protected HashMap<String, Doctor> doctors;
    protected HashMap<UUID, Treatment> treatments;
    protected HashMap<UUID, Visit> visits;

    public Database()
    {
        this.userCredentials = new HashMap<>();
        //DEBUG ONLY
        this.userCredentials.put("Patient-Patient1", new Credentials("Patient1", "Passw0rd."));
        this.userCredentials.put("Doctor-Doctor1", new Credentials("Doctor1", "Passw0rd."));
        this.patients = new HashMap<>();
        this.patients.put("Patient1", new Patient("Patient 1", "patient 1's address goes here", true));
        this.patients.get("Patient1").DoctorUsername = "Doctor1";
        this.doctors = new HashMap<>();
        this.doctors.put("Doctor1", new Doctor("Doctor 1", "doctor 1's address goes here", new boolean[]{true, true, true, true, true, false, false}));
        this.treatments = new HashMap<>();
        this.visits = new HashMap<>();
    }
}
