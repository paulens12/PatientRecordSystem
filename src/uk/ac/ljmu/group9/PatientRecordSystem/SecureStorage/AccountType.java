package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

// There are three types of accounts: patient, dentist (doctor) and practice administrator.
// Technically, the application allows having multiple practice administrators,
// just like doctors and patients, although there is no way to add them in the user interface.
public enum AccountType
{
    Patient ("Patient"), Doctor ("Doctor"), Admin("Admin");

    private String text;

    public String GetText()
    {
        return this.text;
    }

    AccountType(String text)
    {
        this.text = text;
    }
}
