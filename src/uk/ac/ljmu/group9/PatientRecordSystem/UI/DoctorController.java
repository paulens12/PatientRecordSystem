package uk.ac.ljmu.group9.PatientRecordSystem.UI;

public class DoctorController implements IActionController {
    public boolean ExecuteAction(String action) {
        switch(action)
        {
            case "ViewFutureAppointments":
                return ViewFutureAppointments();
            case "ViewPastAppointments":
                return ViewPastAppointments();
            case "ViewPastTreatments":
                return ViewPastTreatments();
            case "AddTreatment":
                return AddTreatment();
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

    private boolean ViewFutureAppointments()
    {
        return true;
    }

    private boolean ViewPastAppointments()
    {
        return true;
    }

    private boolean ViewPastTreatments()
    {
        return true;
    }

    private boolean AddTreatment()
    {
        return true;
    }
}
