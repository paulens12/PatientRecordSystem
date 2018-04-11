package uk.ac.ljmu.group9.PatientRecordSystem;

import uk.ac.ljmu.group9.PatientRecordSystem.UI.*;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        Node rootNode = UserInterface.InitializeMenuStructure();
        UserInterface ui = new UserInterface(rootNode);
        ui.Start();
    }


}


