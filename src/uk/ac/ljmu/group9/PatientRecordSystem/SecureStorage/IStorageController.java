package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import uk.ac.ljmu.group9.PatientRecordSystem.Ailment;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.TreatmentType;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;

// Defines the behaviour or a data storage. The underlying storage might be a SQL database, a text or binary file or
// something else. In our implementation, it's an encrypted binary file.
public interface IStorageController
{
    // Loads the data to memory, typically during application startup.
    void Load();
    // Saves the data to hard drive, typically just before the application exits.
    void Save();
    // Verifies the password of a given user.
    boolean VerifyPassword(AccountType accType, String username, String password) throws IllegalArgumentException;
    // Verifies that the password is strong enough.
    boolean VerifyPasswordStrength(String rawPassword);
    // Returns a list of treatments associated to a given user.
    ArrayList<Treatment> GetTreatments(AccountType accType, String username) throws IllegalArgumentException;
    // Returns a list of past visits associated to a given user.
    ArrayList<Visit> GetPastVisits(AccountType accType, String username) throws IllegalArgumentException;
    // Returns the list of a doctor's future visits.
    ArrayList<Visit> GetFutureVisits(String username) throws IllegalArgumentException;
    // Adds a visit to the storage.
    void AddVisit(LocalDateTime date, String patientUsername, String doctorUsername, Ailment ailment) throws IllegalArgumentException;
    // Adds a treatment to the storage.
    void AddTreatment(LocalDateTime date, String patientUsername, String doctorUsername, TreatmentType treatment) throws IllegalArgumentException;
    // Sets the privacy setting of a patient.
    void SetPrivacy(String username, boolean accessGranted) throws IllegalArgumentException;
    // Returns the current privacy setting of a patient.
    boolean GetPrivacy(String username) throws IllegalArgumentException;
    // Adds a dentist account to the storage.
    void AddDoctor(String username, String name, String address, boolean[] workingDays, String password);
    // Adds a patient account to the storage.
    void AddPatient(String username, String name, String address, String doctorUsername, String password) throws IllegalArgumentException;
    // Removes a dentist account from the storage.
    void RemoveDoctor(String username) throws IllegalArgumentException;
    // Removes a patient account from the storage.
    void RemovePatient(String username) throws IllegalArgumentException;
    // Changes the real name of a user.
    void ChangeName(AccountType accType, String username, String newName) throws IllegalArgumentException;
    // Changes the address of a user.
    void ChangeAddress(AccountType accType, String username, String newAddress) throws IllegalArgumentException;
    // Sets the first choice dentist of a patient.
    void SetDoctor(String patientUsername, String doctorUsername) throws IllegalArgumentException;
    // Sets the working days of a dentist.
    void SetWorkingDays(String username, boolean[] workingDays) throws IllegalArgumentException;
    // Returns the username of the first choice doctor of a patient.
    String GetFirstChoiceDoctor(String patientUsername) throws IllegalArgumentException;
    // Returns the real name of a user.
    String GetUserRealName(AccountType accType, String username) throws IllegalArgumentException;
    // Returns the address of a user.
    String GetUserAddress(AccountType accType, String username) throws IllegalArgumentException;
}
