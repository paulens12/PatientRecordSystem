package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.Ailment;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.time.LocalDateTime;
import java.util.*;

public class PatientController implements IActionController
{
    private IStorageController sc;
    private Scanner scanner;
    private String username;

    public PatientController(IStorageController sc, Scanner scanner)
    {
        this.sc = sc;
        this.scanner = scanner;
    }

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
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();

        if(this.sc.VerifyPassword(AccountType.Patient, username, password))
        {
            this.username = username;
            return true;
        }
        return false;
    }

    private boolean SetPrivacy()
    {
        String enabled = "Everyone can see my appointments";
        String disabled = "My appointments are hidden";
        System.out.println(String.format("Current setting: %s.\n\n1 - %s\n2 - %s", this.sc.GetPrivacy(this.username) ? enabled : disabled, enabled, disabled));
        int selection = scanner.nextInt();
        scanner.nextLine();
        sc.SetPrivacy(this.username, selection == 1 ? true : selection == 2 ? false : sc.GetPrivacy(this.username));
        return true;
    }

    private boolean RequestAppointment()
    {
        System.out.println("Available ailment types:");
        Ailment[] ailments = Ailment.values();
        for(int i=0; i<ailments.length; i++)
        {
            System.out.println(String.format("%d - %s", i+1, ailments[i]));
        }
        System.out.println("Other - cancel");
        int selection = scanner.nextInt();
        if(selection < 1 || selection > ailments.length)
            return false;
        Ailment ailment = ailments[selection-1];

        LocalDateTime current = LocalDateTime.now();

        System.out.println("Please enter the year");
        int year = scanner.nextInt();
        LocalDateTime ldt = LocalDateTime.MAX;
        ldt = ldt.withYear(year);

        if(year < current.getYear() || year > current.getYear() + 1)
        {
            System.out.println("Incorrect year (could be too far)");
            return false;
        }

        System.out.println("Please enter the month");
        int month = scanner.nextInt();
        if(month < 1 || month > 12)
        {
            System.out.println("Incorrect month");
            return false;
        }
        ldt = ldt.withMonth(month);

        Calendar cal = new GregorianCalendar(year, month, 1);
        System.out.println("Please enter the day of month");
        int day = scanner.nextInt();
        if(day < 1 || day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        {
            System.out.println("Incorrect day of month");
            return false;
        }
        ldt = ldt.withDayOfMonth(day);

        System.out.println("Please enter the hours");
        int hours = scanner.nextInt();
        if(hours < 0 || hours > 23)
        {
            System.out.println("Incorrect hour");
            return false;
        }
        ldt = ldt.withHour(hours);

        System.out.println("Please enter the minutes");
        int minutes = scanner.nextInt();
        ldt = ldt.withMinute(minutes);
        if(minutes < 0 || minutes > 59)
        {
            System.out.println("Incorrect minutes");
        }

        if(ldt.isAfter(current))
        {
            System.out.println("This date has passed.");
            return false;
        }

        scanner.nextLine();

        this.sc.AddVisit(ldt, this.username, this.sc.GetFirstChoiceDoctor(this.username), ailment);

        return true;
    }

    private boolean ListTreatments()
    {
        ArrayList<Treatment> treatments = this.sc.GetTreatments(AccountType.Patient, this.username);
        treatments.sort(new Comparator<Treatment>() {
            @Override
            public int compare(Treatment o1, Treatment o2) {
                return o2.Date.compareTo(o1.Date);
            }
        });
        for (Treatment t : treatments)
            System.out.println(String.format("%s: %s by doctor %s", t.Date.toString(), t.Type.GetText(), this.sc.GetUserRealName(AccountType.Doctor, t.doctorUsername)));
        return true;
    }

    private boolean ListVisits()
    {
        ArrayList<Visit> visits = this.sc.GetPastVisits(AccountType.Patient, this.username);
        visits.sort(new Comparator<Visit>() {
            @Override
            public int compare(Visit o1, Visit o2) {
                return o2.Date.compareTo(o1.Date);
            }
        });
        for (Visit v : visits)
            System.out.println(String.format("%s: %s", v.Date.toString(), v.Ailment.GetText()));
        return true;
    }

    private boolean ChangeDetails()
    {
        return true;
    }
}
