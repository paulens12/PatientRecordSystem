package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.IStorageController;
import uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage.SecureStorageController;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface
{
    public static DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Node rootNode;
    private Scanner scanner = new Scanner(System.in);
    private IStorageController storageController;

    public UserInterface()
    {
        this.storageController = new SecureStorageController();
        this.storageController.Load();
        this.rootNode = initializeMenuStructure();
        //this.storageController = new SecureStorageController();
    }

    private Node initializeMenuStructure() {
        PatientController pc = new PatientController(this.storageController, scanner);
        DoctorController dc = new DoctorController(storageController, scanner);
        AdminController ac = new AdminController(storageController, scanner);

        // patient functionality
        Node setPrivacy = new Node("Set privacy setting", pc, "SetPrivacy");
        Node requestAppointment = new Node("Request an appointment with your first choice dentist", pc, "RequestAppointment");
        Node listTreatments = new Node("View your treatment history", pc, "ListTreatments");
        Node listVisits = new Node("List visits", pc, "ListVisits");
        Node changeDetails = new Node("Change your personal details", pc, "ChangeDetails");
        Node patientMenu = new Node("Patient menu", Arrays.asList(listVisits, listTreatments, requestAppointment, setPrivacy, changeDetails));
        Node patientLogin = new Node("Patient login", pc, "Login", patientMenu);

        // dentist functionality
        Node viewFutureAppointments = new Node("View upcoming appointments", dc, "ViewFutureAppointments");
        Node viewPastAppointments = new Node("View past appointments", dc, "ViewPastAppointments");
        Node addTreatment = new Node("Add treatment", dc, "AddTreatment");
        Node changeDetails1 = new Node("Change your personal details", dc, "ChangeDetails");
        Node viewPastTreatments = new Node("View past treatments", dc, "ViewPastTreatments");
        Node doctorMenu = new Node("Dentist menu", Arrays.asList(viewFutureAppointments, viewPastAppointments, addTreatment, viewPastTreatments, changeDetails1));
        Node doctorLogin = new Node("Dentist login", dc, "Login", doctorMenu);

        //admin functionality
        Node listDoctorAppointments = new Node("List a dentist's past appointments", ac, "ListDoctorAppointments");
        Node listDoctorFutureAppointments = new Node("List a dentist's upcoming appointments", ac, "ListDoctorFutureAppointments");
        Node listPatientVisits = new Node("List a patient's past visits", ac, "ListPatientVisits");
        Node listDoctorTreatments = new Node("List a dentist's treatments", ac, "ListDoctorTreatments");
        Node listPatientTreatments = new Node("List a patient's treatments", ac, "ListPatientTreatments");
        Node addDoctor = new Node("Add a new dentist", ac, "AddDoctor");
        Node addPatient = new Node("Add a new patient", ac, "AddPatient");
        Node remDoctor = new Node("Remove a dentist", ac, "RemoveDoctor");
        Node remPatient = new Node("Remove a patient", ac, "RemovePatient");
        Node setDoctor = new Node("Set the first choice dentist of a patient", ac, "SetDoctor");
        Node setWorkingDays = new Node("Set a dentist's working days", ac, "SetWorkingDays");
        Node adminMenu = new Node("Admin menu", Arrays.asList(listDoctorAppointments, listDoctorFutureAppointments, listPatientVisits, listDoctorTreatments, listPatientTreatments, addDoctor, addPatient, remDoctor, remPatient, setDoctor, setWorkingDays));
        Node adminLogin = new Node("Admin login", ac, "Login", adminMenu);
        //Node listTreatments = new Node("", null, null);

        return new Node("Patient Record System", Arrays.asList(patientLogin, doctorLogin, adminLogin));
    }

    // UI initialization
    public void Start()
    {
        Node currentNode = rootNode;
        Node nextNode;
        boolean result;
        while(true)
        {
            if(currentNode.ChildNodes != null)
            {
                currentNode = DisplayMenu(currentNode);
            }
            else
            {
                result = currentNode.ExecuteAction();
                nextNode = currentNode.GetNextNode();
                if(nextNode == currentNode)
                    break;
                if(result)
                    currentNode = nextNode;
                else
                {
                    System.out.println("Operation failed.");
                    currentNode = currentNode.ParentNode;
                }
            }
            if(currentNode == null)
                break;
        }
        this.storageController.Save();
    }

    public static int GetInt(Scanner s)
    {
        try
        {
            int res = s.nextInt();
            s.nextLine();
            return res;
        }
        catch(InputMismatchException e)
        {
            return Integer.MIN_VALUE;
        }
    }

    private Node DisplayMenu(Node current)
    {
        System.out.println("\n" + current.Name + "\n");
        for(int i=0; i<current.ChildNodes.size(); i++)
            System.out.println(String.format("%d. %s", i+1, current.ChildNodes.get(i).Name));
        System.out.println();
        if(current != rootNode)
            System.out.println("0. Previous menu");
        System.out.println("Other - exit application and save data");
        int selection = GetInt(scanner);
        if(selection == 0)
            return current.ParentNode;
        if(selection > 0 && selection <= current.ChildNodes.size())
            return current.ChildNodes.get(selection-1);
        return null;
    }
}
