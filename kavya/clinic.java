import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cms?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Kavya@05";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

class Patient {
    public String PatientName;
    int PatientId;
    String gender,ContactNumber;
    int age;
    public Patient(String PatientName,int age, String gender, String contact,int PatientId) {
        this.PatientName = PatientName;this.age = age; this.gender = gender;this.PatientId = PatientId;
        this.ContactNumber = contact;
    }

}

class Doctor {
    int doctorId;
    String DoctorName, specialization,DcontactNumber;
    int DoctorId;
    public Doctor(String DName, String spec, String contact,int DoctorId) {
        this.DoctorName= DName;
        this.specialization = spec; this.DcontactNumber = contact;this.DoctorId = DoctorId;
    }
}

class Appointment {
    int patientId, DoctorId;
    Date appointmentDate;
    Time appointmentTime;

    public Appointment(int patientId, int DoctorId, Date date, Time time) {
        this.patientId = patientId; this.DoctorId = DoctorId; this.appointmentDate = date;
        this.appointmentTime = time;
    }
}

class PatientDAO {
    public void addPatient(Patient p) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient_doctor_details (PatientName, PatientId, PatientAge, PatientGender, ContactNumber) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, p.PatientName); stmt.setInt(2, p.PatientId); stmt.setInt(3, p.age); stmt.setString(4, p.gender);
            stmt.setString(5, p.ContactNumber);stmt.executeUpdate();
        }
    }
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM patient_doctor_details")) {
            while (rs.next()) {
                patients.add(new Patient(rs.getString("PatientName"),rs.getInt("PatientAge"),
                        rs.getString("PatientGender"), rs.getString("ContactNumber"),rs.getInt("PatientId")));
            }
        }
        return patients;
    }
}

class DoctorDAO {
    public void addDoctor(Doctor d) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient_doctor_details(DoctorName, Specialization, DcontactNumber,DoctorId) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, d.DoctorName); stmt.setString(2, d.specialization); stmt.setString(3, d.DcontactNumber);stmt.setInt(4, d.DoctorId);
            stmt.executeUpdate();
        }
    }
    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM patient_doctor_details")) {
            while (rs.next()) {
                doctors.add(new Doctor(rs.getString("DoctorName"),rs.getString("Specialization"),rs.getString("DContactNumber"),rs.getInt("DoctorId")));
            }
        }
        return doctors;
    }
}

class AppointmentDAO {
    public void addAppointment(Appointment a) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Appointment (PatientID, DoctorID, AppointmentDate, AppointmentTime) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, a.patientId); stmt.setInt(2, a.DoctorId); stmt.setDate(3, a.appointmentDate);
            stmt.setTime(4, a.appointmentTime); stmt.executeUpdate();
        }
    }
    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM patient_doctor_details")) {
            while (rs.next()) {
                appointments.add(new Appointment(rs.getInt("PatientID"), rs.getInt("DoctorID"),
                        rs.getDate("AppointmentDate"), rs.getTime("AppointmentTime")));
            }
        }
        return appointments;
    }
}

public class ClinicManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public static void main(String[] args) throws SQLException {
        int option;
        do {
            System.out.println("\nClinic Management System:");
            System.out.println("1. Add Patient\n2. View Patients\n3. Add Doctor\n4. View Doctors\n5. Schedule Appointment\n6. View Appointments\n7. Exit");
            System.out.print("Enter choice: ");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> addPatient();
                case 2 -> viewPatients();
                case 3 -> addDoctor();
                case 4 -> viewDoctors();
                case 5 -> scheduleAppointment();
                case 6 -> viewAppointments();
                case 7 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (option != 7);
    }

    private static void addPatient() throws SQLException {

        System.out.print("Enter Patient name: "); String PatientName = scanner.next();
        System.out.print("Enter age: "); int age = scanner.nextInt();
        System.out.print("Enter gender: "); String gender = scanner.next();
        System.out.print("Enter contact number: "); String contact = scanner.next();
       // System.out.print("Enter address: "); String address = scanner.next();
        System.out.print("Enter Patient Id: "); int PatientId = scanner.nextInt();

        patientDAO.addPatient(new Patient(PatientName, age, gender,contact,PatientId));
        System.out.println("Patient added.");
    }

    private static void viewPatients() throws SQLException {
        List<Patient> patients = patientDAO.getAllPatients();
        patients.forEach(p -> System.out.println("Name: " + p.PatientName + ", Age: " + p.age + ", Gender: " + p.gender));
    }

    private static void addDoctor() throws SQLException {
        System.out.print("Enter Doctor name: "); String DName = scanner.next();
        System.out.print("Enter Doctor Id: "); int DId = scanner.nextInt();
        System.out.print("Enter specialization: "); String specialization = scanner.next();
        System.out.print("Enter contact number: "); String contact = scanner.next();
        doctorDAO.addDoctor(new Doctor(DName, specialization, contact,DId));
        System.out.println("Doctor added.");
    }

    private static void viewDoctors() throws SQLException {
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        doctors.forEach(d -> System.out.println("Name: " + d.DoctorName+ ", Specialization: " + d.specialization));
    }

    private static void scheduleAppointment() throws SQLException {
        System.out.print("Enter patient ID: "); int patientId = scanner.nextInt();
        System.out.print("Enter doctor ID: "); int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): "); Date date = Date.valueOf(scanner.next());
        System.out.print("Enter appointment time (HH:MM:SS): "); Time time = Time.valueOf(scanner.next());
        appointmentDAO.addAppointment(new Appointment(patientId, doctorId, date, time));
        System.out.println("Appointment scheduled.");
    }

    private static void viewAppointments() throws SQLException {
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        appointments.forEach(a -> System.out.println("Patient ID: " + a.patientId + ", Doctor ID: " + a.DoctorId + ", Date: " + a.appointmentDate));