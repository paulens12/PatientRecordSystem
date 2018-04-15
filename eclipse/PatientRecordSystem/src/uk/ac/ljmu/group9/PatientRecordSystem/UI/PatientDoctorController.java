package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.util.ArrayList;
import java.util.Scanner;

// Handles some UI actions that are common between the dentist and patient menus.
public abstract class PatientDoctorController
{
    AccountType accountType;
    IStorageController sc;
    Scanner scanner;
    String username;

    // Allows the user to change their personal details.
    boolean ChangeDetails()
    {
        String currentName = this.sc.GetUserRealName(this.accountType, this.username);
        String currentAddress = this.sc.GetUserAddress(this.accountType, this.username);
        System.out.println(String.format("Select detail to change:\n1 - Name: %s\n2 - Address: %s\nOther - cancel", currentName, currentAddress));
        String input = this.scanner.nextLine();
        switch(input)
        {
            case "1":
                System.out.println("Please enter your new name");
                input = this.scanner.nextLine();
                this.sc.ChangeName(this.accountType, this.username, input);
                return input.equals(this.sc.GetUserRealName(this.accountType, this.username));
            case "2":
                System.out.println("Please enter your new address");
                input = this.scanner.nextLine();
                this.sc.ChangeAddress(this.accountType, this.username, input);
                return input.equals(this.sc.GetUserAddress(this.accountType, this.username));
            default:
                return true;
        }
    }

    // Handles the log in procedure.
    boolean Login()
    {
        System.out.println("Please enter your username:");
        String username = this.scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = this.scanner.nextLine();

        if (this.sc.VerifyPassword(this.accountType, username, password))
        {
            this.username = username;
            return true;
        }
        return false;
    }

    // Allows the user to view past treatments associated with them.
    protected boolean ListTreatments()
    {

        ArrayList<Treatment> treatments;
        try
        {
            treatments = this.sc.GetTreatments(this.accountType, this.username);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Invalid user.");
            return false;
        }
        treatments.sort((o1, o2) -> o2.Date.compareTo(o1.Date));
        for (Treatment t : treatments)
            try
            {
                System.out.println(
                        String.format("%s: %s %s %s (%s)",
                                t.Date.format(UserInterface.DateFormat),
                                t.Type.GetText(),
                                this.accountType == AccountType.Patient ? "by doctor" : "given to",
                                this.sc.GetUserRealName(this.accountType == AccountType.Patient ? AccountType.Doctor : AccountType.Patient,
                                        this.accountType == AccountType.Patient ? t.doctorUsername : t.patientUsername),
                                this.accountType == AccountType.Patient ? t.doctorUsername : t.patientUsername));
            }
            catch(IllegalArgumentException ignored){}
        System.out.println("End of list. Press <enter> to continue");
        this.scanner.nextLine();
        return true;
    }

    // Allows the user to view past visits associated with them.
    protected boolean ListVisits()
    {
        System.out.println("Past visits:");
        ArrayList<Visit> visits;
        try
        {
            visits = this.sc.GetPastVisits(this.accountType, this.username);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Invalid user.");
            return false;
        }
        visits.sort((o1, o2) -> o2.Date.compareTo(o1.Date));
        for (Visit v : visits)
            System.out.println(String.format("%s: %s%s",
                    v.Date.format(UserInterface.DateFormat),
                    v.Ailment.GetText(),
                    this.accountType == AccountType.Doctor ? String.format(" by patient %s (%s)", this.sc.GetUserRealName(AccountType.Patient, v.Patient), v.Patient) : ""));
        System.out.println("End of list. Press <enter> to continue");
        this.scanner.nextLine();
        return true;
    }
}
