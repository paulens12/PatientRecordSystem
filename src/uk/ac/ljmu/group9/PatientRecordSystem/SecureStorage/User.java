package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class User implements Serializable {
    protected String name = "";
    protected String address = "";
    private List<UUID> treatments = new ArrayList<>();
    private List<UUID> visits = new ArrayList<>();

    public User(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    public List<UUID> GetTreatments()
    {
        return Collections.unmodifiableList(this.treatments);
    }

    public List<UUID> GetVisits()
    {
        return Collections.unmodifiableList(this.visits);
    }

    public void AddTreatment(UUID t)
    {
        this.treatments.add(t);
    }

    public void AddVisit(UUID v)
    {
        this.visits.add(v);
    }

    public String GetName()
    {
        return this.name;
    }

    public String GetAddress()
    {
        return this.address;
    }
}
