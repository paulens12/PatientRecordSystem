package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;
import uk.ac.ljmu.group9.PatientRecordSystem.TreatmentType;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                System.out.println("Treatments you have given:");
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

    protected boolean ListFutureAppointments()
    {
        System.out.println("Your upcoming appointments:");
        ArrayList<Visit> visits;
        try
        {
            visits = this.sc.GetFutureVisits(this.username);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Invalid user.");
            return false;
        }
        visits.sort((o1, o2) -> o2.Date.compareTo(o1.Date));
        for (Visit v : visits)
            System.out.println(String.format("%s: %s - %s", v.Date.format(UserInterface.DateFormat), v.Patient, v.Ailment.GetText()));
        System.out.println("End of list. Press <enter> to continue");
        this.scanner.nextLine();
        return true;
    }

    private boolean AddTreatment()
    {
        System.out.println("Please select the treatment:");
        TreatmentType[] treatmentTypes = TreatmentType.values();
        for (int i = 0; i < treatmentTypes.length; i++) {
            System.out.println(String.format("%d - %s", i + 1, treatmentTypes[i]));
        }
        System.out.println("Other - cancel");
        int selection = UserInterface.GetInt(this.scanner);
        if (selection < 1 || selection > treatmentTypes.length)
            return false;
        TreatmentType treatment = treatmentTypes[selection - 1];

        System.out.println("Please enter the patient's username:");
        String patient = this.scanner.nextLine();
        try
        {
            this.sc.AddTreatment(LocalDateTime.now(), patient, this.username, treatment);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Invalid patient username");
            return false;
        }
        return true;
    }
}
