package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

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
