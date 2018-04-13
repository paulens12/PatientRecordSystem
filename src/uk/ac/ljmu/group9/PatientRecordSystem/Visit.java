package uk.ac.ljmu.group9.PatientRecordSystem;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Visit implements Serializable
{
    public LocalDateTime Date;
    public Ailment Ailment;

    public Visit(LocalDateTime date, Ailment ailment)
    {
        this.Date = date;
        this.Ailment = ailment;
    }
}
