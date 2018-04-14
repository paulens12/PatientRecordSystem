package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Credentials implements Serializable
{
    private String username;
    private byte[] passwordHash;
    private static final String salt = "fjk]ghd,.;]`as7563j\\;rew157p/.d~\"}{fasdas";

    public Credentials(String username, String password)
    {
        this.username = username;
        this.passwordHash = EncryptPassword(password);
    }

    public boolean Verify(String pass)
    {
        return Arrays.equals(this.passwordHash, EncryptPassword(pass));
    }

    public String GetUsername()
    {
        return this.username;
    }

    private byte[] EncryptPassword(String str)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest((str + this.salt).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SetPassword(String newPassword)
    {
        this.passwordHash = EncryptPassword(newPassword);
    }
}
