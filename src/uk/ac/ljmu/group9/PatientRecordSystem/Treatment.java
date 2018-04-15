package uk.ac.ljmu.group9.PatientRecordSystem;

import java.io.Serializable;
import java.time.LocalDateTime;

// Stores information about a treatment.
public class Treatment implements Serializable
{
    public LocalDateTime Date;
    public TreatmentType Type;
    public String doctorUsername;
    public String patientUsername;

    public Treatment(LocalDateTime date, TreatmentType tt, String patientUsername, String doctorUsername)
    {
        this.Date = date;
        this.Type = tt;
        this.doctorUsername = doctorUsername;
        this.patientUsername = patientUsername;
    }
}
