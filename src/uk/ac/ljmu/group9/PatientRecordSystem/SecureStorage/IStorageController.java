package uk.ac.ljmu.group9.PatientRecordSystem.SecureStorage;

import uk.ac.ljmu.group9.PatientRecordSystem.Ailment;
import uk.ac.ljmu.group9.PatientRecordSystem.Treatment;
import uk.ac.ljmu.group9.PatientRecordSystem.TreatmentType;
import uk.ac.ljmu.group9.PatientRecordSystem.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IStorageController
{
    void Load();
    void Save();
    boolean VerifyPassword(AccountType accType, String username, String password) throws IllegalArgumentException;
    boolean VerifyPasswordStrength(String rawPassword);
    ArrayList<Treatment> GetTreatments(AccountType accType, String username) throws IllegalArgumentException;
    ArrayList<Visit> GetPastVisits(AccountType accType, String username) throws IllegalArgumentException;
    ArrayList<Visit> GetFutureVisits(String username) throws IllegalArgumentException;
    void AddVisit(LocalDateTime date, String patientUsername, String doctorUsername, Ailment ailment) throws IllegalArgumentException;
    void AddTreatment(LocalDateTime date, String patientUsername, String doctorUsername, TreatmentType treatment) throws IllegalArgumentException;
    void SetPrivacy(String username, boolean accessGranted) throws IllegalArgumentException;
    boolean GetPrivacy(String username) throws IllegalArgumentException;
    void AddDoctor(String username, String name, String address, boolean[] workingDays, String password);
    void AddPatient(String username, String name, String address, String doctorUsername, String password) throws IllegalArgumentException;
    void RemoveDoctor(String username) throws IllegalArgumentException;
    void RemovePatient(String username) throws IllegalArgumentException;
    void ChangeName(AccountType accType, String username, String newName) throws IllegalArgumentException;
    void ChangeAddress(AccountType accType, String username, String newAddress) throws IllegalArgumentException;
    void SetDoctor(String patientUsername, String doctorUsername) throws IllegalArgumentException;
    void SetWorkingDays(String username, boolean[] workingDays) throws IllegalArgumentException;
    String GetFirstChoiceDoctor(String patientUsername) throws IllegalArgumentException;
    String GetUserRealName(AccountType accType, String username) throws IllegalArgumentException;
    String GetUserAddress(AccountType accType, String username) throws IllegalArgumentException;
}
