package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Credentials
{
    private String username;
    private byte[] passwordHash;
    private String salt = "fjk]ghd,.;]`as7563j\\;rew157p/.d~\"}{fasdas";

    public Credentials(String username, String password) throws NoSuchAlgorithmException
    {
        this.username = username;
        this.passwordHash = CreateHash(password);
    }

    public boolean Verify(String pass)
    {
        try
        {
            return this.passwordHash.equals(CreateHash(pass));
        }
        catch (NoSuchAlgorithmException e)
        {
            return false;
        }
    }

    public String GetUsername()
    {
        return this.username;
    }

    private byte[] CreateHash(String str) throws NoSuchAlgorithmException
    {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA-256");
        return digest.digest((str + this.salt).getBytes(StandardCharsets.UTF_8));
    }
}
