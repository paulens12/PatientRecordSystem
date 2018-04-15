package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// This class stores and verifies the passwords for all users.
public class Credentials implements Serializable
{
    private String username;

    // Passwords are stored in the form of a hash, so there is no way to decrypt them even if the database is
    // compromised.
    private byte[] passwordHash;

    // The salt is stored as a static variable, so it will not be serialized. That means it can only be found in the
    // program code, but not in the database file, making it harder for a hacker to find both the password hash and
    // the salt.
    private static final String salt = "fjk]ghd,.;]`as7563j\\;rew157p/.d~\"}{fasdas";

    public Credentials(String username, String password)
    {
        this.username = username;
        this.passwordHash = EncryptPassword(password);
    }

    // This method verifies whether the password is correct.
    public boolean Verify(String pass)
    {
        return Arrays.equals(this.passwordHash, EncryptPassword(pass));
    }

    // Returns the username.
    public String GetUsername()
    {
        return this.username;
    }

    // Returns the hash of a raw password.
    private byte[] EncryptPassword(String str)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest((str + salt).getBytes(StandardCharsets.UTF_8));
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
