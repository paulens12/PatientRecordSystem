package uk.ac.ljmu.group9.PatientRecordSystem;

import java.io.Serializable;
import java.time.LocalDateTime;

// Stores information about a visit.
public class Visit implements Serializable
{
    public LocalDateTime Date;
    public Ailment Ailment;
    public String Patient;

    public Visit(LocalDateTime date, Ailment ailment, String patient)
    {
        this.Date = date;
        this.Ailment = ailment;
        this.Patient = patient;
    }
}
