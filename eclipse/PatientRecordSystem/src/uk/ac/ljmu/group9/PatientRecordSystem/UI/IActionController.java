package uk.ac.ljmu.group9.PatientRecordSystem.UI;

// Defines a control class that handles specific UI functionality.
public interface IActionController
{
    // Selects the right action to execute and returns its result.
    public boolean ExecuteAction(String action);
}
