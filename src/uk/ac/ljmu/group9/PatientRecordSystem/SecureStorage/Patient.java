package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

public class Patient extends User {
    public boolean PrivacySetting;
    public String DoctorUsername;
    public Patient(String name, String address, boolean privacySetting)
    {
        super(name, address);
        this.PrivacySetting = privacySetting;
    }
}
