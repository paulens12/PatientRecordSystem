package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

public class Patient extends User {
    public boolean PrivacySetting;
    // The first choice dentist of this user.
    public String DoctorUsername;
    public Patient(String name, String address, String doctorUsername)
    {
        super(name, address);
        this.DoctorUsername = doctorUsername;
        this.PrivacySetting = true;
    }
}
