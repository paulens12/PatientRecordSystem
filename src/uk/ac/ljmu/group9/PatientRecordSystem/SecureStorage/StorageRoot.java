package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class StorageRoot implements java.io.Serializable
{
    private HashMap<String, Credentials> userCredentials;

    public void AddCredentials(AccountType type, String username, String pass) throws NoSuchAlgorithmException
    {
        this.userCredentials.put(type + "-" + username, new Credentials(username, pass));
    }


}
