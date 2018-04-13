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
    boolean VerifyPassword(AccountType accType, String username, String password);
    void ChangePassword(AccountType accType, String username, String newPassword);
    boolean VerifyPasswordStrength(String rawPassword);
    ArrayList<Treatment> GetTreatments(AccountType accType, String username);
    ArrayList<Visit> GetPastVisits(AccountType accType, String username);
    ArrayList<Visit> GetFutureVisits(String username);
    void AddVisit(LocalDateTime date, String patientUsername, String doctorUsername, Ailment ailment);
    void AddTreatment(LocalDateTime date, String patientUsername, String doctorUsername, TreatmentType treatment);
    void SetPrivacy(String username, boolean accessGranted);
    boolean GetPrivacy(String username);
    void AddDoctor(String username, String name, String address, boolean[] workingDays, String password);
    void AddPatient(String username, String name, String address, String doctorUsername, String password);
    void RemoveDoctor(String username);
    void RemovePatient(String username);
    void ChangeName(AccountType accType, String username, String newName);
    void ChangeAddress(AccountType accType, String username, String newAddress);
    void SetDoctor(String patientUsername, String doctorUsername);
    void SetWorkingDays(String username, boolean[] workingDays);
    String GetFirstChoiceDoctor(String patientUsername);
    void SetFirstChoiceDoctor(String patientUsername, String DoctorUsername);
    String GetUserRealName(AccountType accType, String username);
}
