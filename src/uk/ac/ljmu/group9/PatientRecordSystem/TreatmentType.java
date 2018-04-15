package uk.ac.ljmu.group9.PatientRecordSystem;

// There are six types of treatments: cleaning, crown, tooth extraction, filing, operation and something else.
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
