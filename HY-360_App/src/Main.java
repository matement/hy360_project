import Backend.EmployeeManager;
import DAO.ReportDAO;
import DAO.SalaryRateDAO;
import JDBC.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== ΕΝΑΡΞΗ ΠΡΟΣΟΜΟΙΩΣΗΣ ΠΑΝΕΠΙΣΤΗΜΙΟΥ ===\n");

        // 1. ΑΡΧΙΚΟΠΟΙΗΣΗ: Δημιουργία Τμημάτων και Βασικών Μισθών
        setupDatabaseData();

        EmployeeManager manager = new EmployeeManager();
        ReportDAO reportDAO = new ReportDAO();

        // Ημερομηνία πρόσληψης (1η του τρέχοντος μήνα)
        Date startDate = Date.valueOf(LocalDate.now().withDayOfMonth(1));

        // ---------------------------------------------------------
        // ΒΗΜΑ 2: ΠΡΟΣΛΗΨΕΙΣ (Hiring)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 1: Προσλήψεις Προσωπικού ---");

        // Α. Μόνιμος Καθηγητής (ID: 101) - Παντρεμένος, 10 χρόνια εμπειρία
        manager.hirePermanentTeaching(
                101, "Γιώργος Παπαδόπουλος", "Κνωσσού 50", "GR101", "6971234567",
                true, "Piraeus Bank", "Computer Science", startDate, 10, 200.0
        );

        // Β. Μόνιμος Διοικητικός (ID: 102) - Ανύπαντρη, 2 χρόνια εμπειρία
        manager.hirePermanentAdministrative(
                102, "Μαρία Οικονόμου", "Εθνικής Αντιστάσεως 10", "GR102", "6981234567",
                false, "Alpha Bank", "Mathematics", startDate, 2
        );

        // Γ. Συμβασιούχος Καθηγητής (ID: 103) - Επίδομα βιβλιοθήκης 50€
        manager.hireContractorTeaching(
                103, "Κώστας Συμβασιούχος", "Λεωφόρος Ικάρου 5", "GR103", "6991234567",
                true, "Eurobank", "Physics", startDate, 50.0
        );
        // ΠΡΟΣΟΧΗ: Οι συμβασιούχοι χρειάζονται και εγγραφή στον πίνακα Contract.
        // Θεωρούμε ότι η hireContractorTeaching το έκανε ή το κάνουμε χειροκίνητα αν λείπει.
        // Εδώ θα υποθέσω ότι το σύστημα χρειάζεται "Ανανέωση" για να ορίσει μισθό/διάρκεια αν δεν μπήκε αυτόματα.
        manager.renewContract(103, startDate, Date.valueOf(LocalDate.now().plusMonths(6)), 1200.00);

        // Δ. Συμβασιούχος Διοικητικός (ID: 104)
        manager.hireContractorAdministrative(
                104, "Ελένη Δημητρίου", "Πλατεία Ελευθερίας 1", "GR104", "6901234567",
                false, "National Bank", "Computer Science", startDate
        );
        manager.renewContract(104, startDate, Date.valueOf(LocalDate.now().plusMonths(12)), 900.00);

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 3: ΤΡΟΠΟΠΟΙΗΣΕΙΣ (Updates & Changes)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 2: Αλλαγές Στοιχείων ---");

        // Προσθήκη Παιδιών (Ο Παπαδόπουλος αποκτά 2 παιδιά -> +10% επίδομα)
        manager.addChild(101, "Νικολάκης", 5);
        manager.addChild(101, "Ελενίτσα", 8);

        // Αλλαγή Διεύθυνσης
        manager.updateEmployeeDetails(102, "Νέα Διεύθυνση 100", "6989999999", "GR102-NEW", "Alpha Bank", true); // Παντρεύτηκε κιόλας!

        // Αύξηση Βασικού Μισθού Μόνιμων Καθηγητών
        SalaryRateDAO salaryDAO = new SalaryRateDAO();
        salaryDAO.UpdateBaseSalary("PERMANENT_TEACHING", 1500.00); // Αύξηση

        // Αύξηση Ειδικού Επιδόματος Έρευνας για τον 101
        manager.updateResearchAllowance(101, 350.0);

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 4: ΜΙΣΘΟΔΟΣΙΑ (Payroll Execution)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 3: Εκτέλεση Μισθοδοσίας ---");

        manager.ExecutePayroll(); // Υπολογίζει και αποθηκεύει πληρωμές

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 5: ΑΝΑΦΟΡΕΣ (Reporting)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 4: Reports & Statistics ---");

        // Α. Ιστορικό Πληρωμών για τον Καθηγητή (101)
        System.out.println(">> Ιστορικό Πληρωμών Υπαλλήλου 101:");
        for (String record : reportDAO.getPaymentHistory(101)) {
            System.out.println(record);
        }

        // Β. Στατιστικά Μισθών ανά Ρόλο
        System.out.println("\n>> Στατιστικά Μισθών ανά Κατηγορία:");
        Map<String, String> stats = reportDAO.getSalaryStatsPerRole();
        for (String role : stats.keySet()) {
            System.out.println(role + ": " + stats.get(role));
        }

        // Γ. Συνολικό Κόστος Μισθοδοσίας Τρέχοντος Μήνα
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        double totalCost = reportDAO.getTotalPayrollCost(currentMonth, currentYear);
        System.out.println("\n>> Συνολικό Κόστος Μισθοδοσίας (" + currentMonth + "/" + currentYear + "): " + totalCost + "€");

        // Δ. Κόστος ανά Ρόλο
        System.out.println("\n>> Ανάλυση Κόστους ανά Ρόλο:");
        Map<String, Double> costPerRole = reportDAO.getTotalCostPerRole(currentMonth, currentYear);
        for (String role : costPerRole.keySet()) {
            System.out.println(role + ": " + costPerRole.get(role) + "€");
        }


        // ---------------------------------------------------------
        // ΒΗΜΑ 6: ΑΠΟΛΥΣΗ (Firing)
        // ---------------------------------------------------------
        System.out.println("\n--- ΦΑΣΗ 5: Απόλυση Υπαλλήλου ---");
        manager.fireEmployee(104); // Απόλυση της Συμβασιούχου Διοικητικού

        System.out.println("\n=== ΤΕΛΟΣ ΠΡΟΣΟΜΟΙΩΣΗΣ ===");
    }

    // Βοηθητική μέθοδος για να είμαστε σίγουροι ότι υπάρχουν τα απαραίτητα δεδομένα στη βάση
    private static void setupDatabaseData() {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Departments
            String[] depts = {"Computer Science", "Mathematics", "Physics", "Chemistry"};
            PreparedStatement psDept = conn.prepareStatement("INSERT IGNORE INTO Department VALUES (?)");
            for (String d : depts) {
                psDept.setString(1, d);
                psDept.executeUpdate();
            }

            // 2. Salary Rates (Αν δεν υπάρχουν)
            // Χρησιμοποιούμε INSERT IGNORE για να μην σκάσει αν τρέξει 2η φορά
            String sqlRate = "INSERT IGNORE INTO SalaryRates (role_name, base_salary) VALUES (?, ?)";
            PreparedStatement psRate = conn.prepareStatement(sqlRate);

            psRate.setString(1, "PERMANENT_TEACHING");
            psRate.setDouble(2, 1200.00);
            psRate.executeUpdate();

            psRate.setString(1, "PERMANENT_ADMINISTRATIVE");
            psRate.setDouble(2, 900.00);
            psRate.executeUpdate();

            // Οι Contractors δεν έχουν "Base Salary" στον πίνακα SalaryRates, παίρνουν από το Contract,
            // αλλά αν το σύστημά σου το ζητάει για λόγους συνέπειας, πρόσθεσέ το.

            System.out.println("[System]: Database setup completed (Departments & Rates checked).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}