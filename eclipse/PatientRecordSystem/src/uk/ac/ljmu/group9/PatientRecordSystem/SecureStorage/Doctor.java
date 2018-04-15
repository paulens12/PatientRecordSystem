package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

public class Doctor extends User {
    // Working days are stored as a boolean array, where each index means a week day, from 0 as Monday to 6 as Sunday.
    private boolean[] workingDays = new boolean[7];

    public boolean GetWorkingDay(int day) throws IllegalArgumentException
    {
        if(day <= 0 || day > 7)
            throw new IllegalArgumentException();
        return workingDays[day-1];
    }

    public void SetWorkingDays(boolean[] days)
    {
        if(days.length != 7)
            return;
        this.workingDays = days.clone();
    }

    public Doctor(String name, String address, boolean[] workingDays)
    {
        super(name, address);
        SetWorkingDays(workingDays);
    }
}
