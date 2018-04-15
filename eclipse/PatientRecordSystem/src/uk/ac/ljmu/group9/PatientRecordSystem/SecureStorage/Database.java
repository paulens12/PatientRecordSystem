package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.util.HashMap;
import java.util.UUID;

import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

// Acts as the global database to be serialized.
public class Database implements java.io.Serializable
{
    HashMap<String, Credentials> userCredentials;
    HashMap<String, Patient> patients;
    HashMap<String, Doctor> doctors;
    HashMap<UUID, Treatment> treatments;
    HashMap<UUID, Visit> visits;

    Database()
    {
        this.userCredentials = new HashMap<>();
        this.patients = new HashMap<>();
        this.doctors = new HashMap<>();
        this.treatments = new HashMap<>();
        this.visits = new HashMap<>();
        // The following entries are overwritten if the database file is found. They are left there as a backup
        // in case the database file is missing or corrupt.
        this.userCredentials.put("Patient-Patient1", new Credentials("Patient1", "Passw0rd."));
        this.userCredentials.put("Doctor-Doctor1", new Credentials("Doctor1", "Passw0rd."));
        this.userCredentials.put("Admin-admin", new Credentials("admin", "admin"));
        this.patients.put("Patient1", new Patient("Patient 1", "patient 1's address goes here", "Doctor1"));
        this.doctors.put("Doctor1", new Doctor("Doctor 1", "doctor 1's address goes here", new boolean[]{true, true, true, true, true, true, false}));
    }
}