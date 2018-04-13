package uk.ac.ljmu.group9.PatientRecordSystem;

public enum TreatmentType
{
    Cleaning("Cleaning"), Crown("Crown"), Extraction("Tooth extraction"), Filing("Filing"), Operation("Operation"), Other("Other");

    private String text;

    public String GetText()
    {
        return this.text;
    }

    TreatmentType(String text)
    {
        this.text = text;
    }
}
