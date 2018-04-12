package uk.ac.ljmu.group9.PatientRecordSystem.UI;

public class PatientController implements IActionController
{
    public boolean ExecuteAction(String action)
    {
        switch(action)
        {
            case "SetPrivacy":
                return SetPrivacy();
            case "RequestAppointment":
                return RequestAppointment();
            case "ListTreatments":
                return ListTreatments();
            case "ListVisits":
                return ListVisits();
            case "ChangeDetails":
                return ChangeDetails();
            case "Login":
                return Login();
            default:
                return false;
        }
    }

    private boolean Login()
    {
        return true;
    }

    private boolean SetPrivacy()
    {
        return true;
    }

    private boolean RequestAppointment()
    {
        return true;
    }

    private boolean ListTreatments()
    {
        return true;
    }

    private boolean ListVisits()
    {
        return true;
    }

    private boolean ChangeDetails()
    {
        return true;
    }
}
