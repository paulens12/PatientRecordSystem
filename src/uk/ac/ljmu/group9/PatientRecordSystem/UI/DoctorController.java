package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;

import java.util.Scanner;

public class DoctorController implements IActionController
{
    private IStorageController sc;
    private Scanner scanner;

    public DoctorController(IStorageController sc, Scanner scanner)
    {
        this.sc = sc;
        this.scanner = scanner;
    }

    public boolean ExecuteAction(String action)
    {
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
            case "ChangeDetails":
                return ChangeDetails();
            default:
                return false;
        }
    }

    private boolean ChangeDetails()
    {
        return true;
    }

    private boolean Login()
    {
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();

        return this.sc.VerifyPassword(AccountType.Doctor, username, password);
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
