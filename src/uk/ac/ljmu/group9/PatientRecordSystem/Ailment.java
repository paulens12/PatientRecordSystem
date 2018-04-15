package uk.ac.ljmu.group9.PatientRecordSystem;

// There are five types of ailments: tootheache, accident, bleeding, over-sensitive and something else.
public enum Ailment
{
    Toothache("Toothache"), Accident("Accident"), Bleeding("Bleeding"), Oversensitive("Over-sensitive"), Other("Other");

    private String text;

    public String GetText()
    {
        return this.text;
    }

    Ailment(String text)
    {
        this.text = text;
    }
}
