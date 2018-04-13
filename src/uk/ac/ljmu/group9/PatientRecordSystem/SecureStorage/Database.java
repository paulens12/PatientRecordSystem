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
        this.doctors = new HashMap<>();
        this.treatments = new HashMap<>();
        this.visits = new HashMap<>();
    }
}
