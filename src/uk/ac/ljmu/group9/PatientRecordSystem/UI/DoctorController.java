package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;

import java.util.Scanner;

public class DoctorController extends PatientDoctorController implements IActionController
{
    public DoctorController(IStorageController sc, Scanner scanner)
    {
        this.sc = sc;
        this.scanner = scanner;
        this.accountType = AccountType.Doctor;
    }

    public boolean ExecuteAction(String action)
    {
        switch(action)
        {
            case "ViewFutureAppointments":
                return ListFutureAppointments();
            case "ViewPastAppointments":
                return ListVisits();
            case "ViewPastTreatments":
                return ListTreatments();
            case "AddTreatment":
                return AddTreatment();
            case "Login":
                return Login();
            case "ChangeDetails":
                return ChangeDetails();
            default:
                return false;
        }
    }

    private boolean ListFutureAppointments()
    {
        return true;
    }

    private boolean AddTreatment()
    {
        return true;
    }
}
