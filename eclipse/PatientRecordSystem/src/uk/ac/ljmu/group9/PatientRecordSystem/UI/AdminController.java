package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;

import java.util.ArrayList;
import java.util.Scanner;

// Handles all the UI actions from the practice administrator menu.
public class AdminController implements IActionController
{
    private IStorageController sc;
    private Scanner scanner;

    public AdminController(IStorageController sc, Scanner scanner)
    {
        this.sc = sc;
        this.scanner = scanner;
    }

    public boolean ExecuteAction(String action)
    {
        switch(action)
        {
            case "ListDoctorAppointments":
                return ListDoctorAppointments();
            case "ListPatientVisits":
                return ListPatientVisits();
            case "ListDoctorTreatments":
                return ListDoctorTreatments();
            case "ListPatientTreatments":
                return ListPatientTreatments();
            case "AddDoctor":
                return AddDoctor();
            case "AddPatient":
                return AddPatient();
            case "RemoveDoctor":
                return RemoveDoctor();
            case "RemovePatient":
                return RemovePatient();
            case "SetDoctor":
                return SetDoctor();
            case "SetWorkingDays":
                return SetWorkingDays();
            case "Login":
                return Login();
            case "ListDoctorFutureAppointments":
                return ListDoctorFutureAppointments();
            default:
                return false;
        }
    }

    // Allows the practice administrator to view a dentist's future appointments.
    private boolean ListDoctorFutureAppointments()
    {
        System.out.println("Enter the dentist's username:");
        String doctor = this.scanner.nextLine();

        DoctorController dc = new DoctorController(this.sc, this.scanner);
        dc.username = doctor;
        return dc.ListFutureAppointments();
    }

    // Handles the log in procedure for the practice administrator.
    private boolean Login()
    {
        System.out.println("Please enter your username:");
        String username = this.scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = this.scanner.nextLine();

        if (this.sc.VerifyPassword(AccountType.Admin, username, password))
            return true;
        return false;
    }

    // Allows the practice administrator to adjust a dentist's working days.
    private boolean SetWorkingDays()
    {
        //TODO: nice idiot-proof description.
        boolean[] workingDays = getWorkingDays();
        if(workingDays == null)
            return false;

        System.out.println("Enter the dentist's username");
        String username = this.scanner.nextLine();

        try
        {
            this.sc.SetWorkingDays(username, workingDays);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("That dentist does not exist");
            return false;
        }
        return true;
    }

    // Allows the practice administrator to set the first choice dentist of a patient.
    private boolean SetDoctor()
    {
        System.out.println("Enter the patient's username:");
        String patient = this.scanner.nextLine();
        System.out.println("Enter the dentist's username:");
        String doctor = this.scanner.nextLine();

        try
        {
            this.sc.SetDoctor(patient, doctor);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Please make sure you enter valid usernames");
            return false;
        }
        return true;
    }

    // Allows the practice administrator to remove a patient from the system.
    private boolean RemovePatient()
    {
        System.out.println("Enter the patient's username:");
        String patient = this.scanner.nextLine();

        try
        {
            this.sc.RemovePatient(patient);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Invalid username");
            return false;
        }
        return true;
    }

    // Allows the practice administrator to remove a dentist from the system.
    private boolean RemoveDoctor()
    {
        System.out.println("Enter the dentist's username:");
        String doctor = this.scanner.nextLine();

        try
        {
            this.sc.RemoveDoctor(doctor);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Invalid username");
            return false;
        }
        return true;
    }

    // Allows the practice administrator to add a patient to the system.
    private boolean AddPatient()
    {
        String name, address, username, password, doctor;

        System.out.println("Enter a username for the new patient:");
        username = this.scanner.nextLine();

        password = getPassword();
        if(password == null)
            return false;
        System.out.println("Enter the real name of the patient:");
        name = this.scanner.nextLine();

        System.out.println("Enter the address of the patient:");
        address = this.scanner.nextLine();

        System.out.println("Enter the first choice dentist's username:");
        doctor = this.scanner.nextLine();

        try
        {
            sc.AddPatient(username, name, address, doctor, password);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("That doctor does not exist.");
            return false;
        }
        return true;
    }

    // Allows the practice administrator to add a dentist to the system.
    private boolean AddDoctor()
    {
        String name, address, username, password;
        boolean[] workingDays;

        System.out.println("Enter a username for the new dentist:");
        username = this.scanner.nextLine();

        password = getPassword();
        if(password == null)
            return false;

        workingDays = getWorkingDays();
        if(workingDays == null)
            return false;

        System.out.println("Enter the real name of the dentist:");
        name = this.scanner.nextLine();

        System.out.println("Enter the address of the dentist:");
        address = this.scanner.nextLine();

        this.sc.AddDoctor(username, name, address, workingDays, password);
        return true;
    }

    // Prompts the practice administrator to enter working days.
    private boolean[] getWorkingDays()
    {
        boolean[] days = new boolean[7];
        int choice;
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        System.out.println("For each week day, select whether the dentist works that day (1 - yes, 2 - no, other - cancel).");
        for(int i=0; i<7; i++)
        {
            System.out.print(dayNames[i] + ": ");
            choice = UserInterface.GetInt(this.scanner);
            System.out.println();
            switch(choice)
            {
                case 1:
                    days[i] = true;
                    break;
                case 2:
                    days[i] = false;
                    break;
                default:
                    return null;
            }
        }
        return days;
    }

    // Prompts the practice administrator to enter a password and checks its strength.
    private String getPassword()
    {
        System.out.println("Enter a password:");
        String password;
        password = this.scanner.nextLine();
        int passwordAttempts = 0;
        while(!this.sc.VerifyPasswordStrength(password))
        {
            System.out.println("Password too weak. Enter a new one:");
            password = this.scanner.nextLine();
            if(++passwordAttempts > 3)
            {
                System.out.println("Too many attempts.");
                return null;
            }
        }
        return password;
    }

    // Allows the practice administrator to view a patient's treatment history.
    private boolean ListPatientTreatments()
    {
        System.out.println("Enter the patient's username:");
        String patient = this.scanner.nextLine();

        PatientController pc = new PatientController(this.sc, this.scanner);
        pc.username = patient;
        if(this.sc.GetPrivacy(patient) == false)
        {
            System.out.println("This patient has chosen not to show their treatments to the practice administrator.");
            return false;
        }
        System.out.println("Treatments received:");
        return pc.ListTreatments();
    }

    // Allows the practice administrator to view a dentist's treatment history.
    private boolean ListDoctorTreatments()
    {
        System.out.println("Enter the dentist's username:");
        String doctor = this.scanner.nextLine();

        DoctorController dc = new DoctorController(this.sc, this.scanner);
        dc.username = doctor;
        System.out.println("Treatments received:");
        ArrayList<Treatment> treatments;
        try
        {
            treatments = this.sc.GetTreatments(AccountType.Doctor, doctor);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Invalid user.");
            return false;
        }
        treatments.sort((o1, o2) -> o2.Date.compareTo(o1.Date));
        for (Treatment t : treatments)
            if(this.sc.GetPrivacy(t.patientUsername))
            System.out.println(String.format("%s: %s given to %s", t.Date.format(UserInterface.DateFormat), t.Type.GetText(), this.sc.GetUserRealName(AccountType.Patient, t.patientUsername)));
        System.out.println("End of list. Press <enter> to continue");
        this.scanner.nextLine();
        return true;
    }

    // Allows the practice administrator to view a patient's past visits.
    private boolean ListPatientVisits()
    {
        System.out.println("Enter the patient's username:");
        String patient = this.scanner.nextLine();

        PatientController pc = new PatientController(this.sc, this.scanner);
        pc.username = patient;
        return pc.ListVisits();
    }

    // Allows the practice administrator to view a dentist's past visits.
    private boolean ListDoctorAppointments()
    {
        System.out.println("Enter the dentist's username:");
        String doctor = this.scanner.nextLine();

        DoctorController dc = new DoctorController(this.sc, this.scanner);
        dc.username = doctor;
        return dc.ListVisits();
    }

}
