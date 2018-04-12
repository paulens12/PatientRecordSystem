package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import java.util.Arrays;
import java.util.Scanner;

public class UserInterface {
    private Node rootNode;
    private Scanner scanner = new Scanner(System.in);

    public UserInterface(Node rootNode)
    {
        this.rootNode = rootNode;
    }

    public static Node InitializeMenuStructure() {
        PatientController pc = new PatientController();
        DoctorController dc = new DoctorController();

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
        Node viewPastTreatments = new Node("View past treatments", dc, "ViewPastTreatments");
        Node doctorMenu = new Node("Dentist menu", Arrays.asList(viewFutureAppointments, viewPastAppointments, addTreatment, viewPastTreatments));
        Node doctorLogin = new Node("Dentist login", dc, "Login", doctorMenu);

        //admin functionality
        //Node listTreatments = new Node("", null, null);

        return new Node("Patient Record System", Arrays.asList(patientMenu, doctorMenu));
    }

    public void Start()
    {
        Node currentNode = rootNode;
        Node nextNode = null;
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
                    System.out.println("Operation failed. Please try again.");
                    currentNode = currentNode.ParentNode;
                }
            }
            if(currentNode == null)
                break;
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
        System.out.println("Other - exit application");
        int selection = scanner.nextInt();
        if(selection == 0)
            return current.ParentNode;
        if(selection > 0 && selection <= current.ChildNodes.size())
            return current.ChildNodes.get(selection-1);
        return null;
    }
}
