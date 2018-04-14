package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.Ailment;
import uk.ac.ljmu.group9.PatientRecordSystem.OutsideWorkingDaysException;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.AccountType;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class PatientController extends PatientDoctorController implements IActionController
{
    public PatientController(IStorageController sc, Scanner scanner)
    {
        this.sc = sc;
        this.scanner = scanner;
        this.accountType = AccountType.Patient;
    }

    public boolean ExecuteAction(String action)
    {
        switch (action) {
            case "SetPrivacy":
                return SetPrivacy();
            case "RequestAppointment":
                return RequestAppointment();
            case "ListTreatments":
                System.out.println("Treatments you have received:");
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

    private boolean SetPrivacy()
    {
        String enabled = "The practice administrator can see my treatments";
        String disabled = "My treatments are hidden";
        System.out.println(String.format("Current setting: %s.\n\n1 - %s\n2 - %s", this.sc.GetPrivacy(this.username) ? enabled : disabled, enabled, disabled));
        int selection = UserInterface.GetInt(this.scanner);
        this.sc.SetPrivacy(this.username, selection == 1 || selection != 2 && this.sc.GetPrivacy(this.username));
        return true;
    }

    private boolean RequestAppointment()
    {
        System.out.println("Available ailment types:");
        Ailment[] ailments = Ailment.values();
        for (int i = 0; i < ailments.length; i++) {
            System.out.println(String.format("%d - %s", i + 1, ailments[i]));
        }
        System.out.println("Other - cancel");
        int selection = UserInterface.GetInt(this.scanner);
        if (selection < 1 || selection > ailments.length)
            return false;
        Ailment ailment = ailments[selection - 1];

        LocalDateTime current = LocalDateTime.now();

        System.out.println("Please enter the year");
        int year = UserInterface.GetInt(this.scanner);
        LocalDateTime ldt = LocalDateTime.MIN;
        ldt = ldt.withYear(year);

        if (year < current.getYear() || year > current.getYear() + 1) {
            System.out.println("Incorrect year (could be too far)");
            return false;
        }

        System.out.println("Please enter the month");
        int month = UserInterface.GetInt(this.scanner);
        if (month < 1 || month > 12) {
            System.out.println("Incorrect month");
            return false;
        }
        ldt = ldt.withMonth(month);

        Calendar cal = new GregorianCalendar(year, month, 1);
        System.out.println("Please enter the day of month");
        int day = UserInterface.GetInt(this.scanner);
        if (day < 1 || day > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            System.out.println("Incorrect day of month");
            return false;
        }
        ldt = ldt.withDayOfMonth(day);

        System.out.println("Please enter the hours");
        int hours = UserInterface.GetInt(this.scanner);
        if (hours < 0 || hours > 23) {
            System.out.println("Incorrect hour");
            return false;
        }
        ldt = ldt.withHour(hours);

        System.out.println("Please enter the minutes");
        int minutes = UserInterface.GetInt(this.scanner);
        ldt = ldt.withMinute(minutes);
        if (minutes < 0 || minutes > 59) {
            System.out.println("Incorrect minutes");
        }

        if (ldt.isBefore(current)) {
            System.out.println("This date has passed.");
            return false;
        }

        try
        {
            this.sc.AddVisit(ldt, this.username, this.sc.GetFirstChoiceDoctor(this.username), ailment);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("You don't have a first choice dentist assigned, please contact the administrator.");
            return false;
        }
        catch(OutsideWorkingDaysException e)
        {
            System.out.println("Your first choice dentist doesn't work that day");
            return false;
        }
        return true;
    }

}
