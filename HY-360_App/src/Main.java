import Backend.EmployeeManager;
import DAO.ReportDAO;
import DAO.SalaryRateDAO;
import JDBC.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== ΕΝΑΡΞΗ ΠΡΟΣΟΜΟΙΩΣΗΣ ΠΑΝΕΠΙΣΤΗΜΙΟΥ ===\n");

        // 0. ΚΑΘΑΡΙΣΜΟΣ ΒΑΣΗΣ (Reset)
        // Εκτελεί τη λογική του hy360_free.sql για να μην έχουμε Duplicate Keys
        JDBC.DatabaseHelper.clearDatabase();

        // 1. ΑΡΧΙΚΟΠΟΙΗΣΗ: Δημιουργία Τμημάτων και Ρόλων (SalaryRates)
        JDBC.DatabaseHelper.setupDatabaseData();

        EmployeeManager manager = new EmployeeManager();
        ReportDAO reportDAO = new ReportDAO();

        // Ημερομηνία πρόσληψης (1η του τρέχοντος μήνα)
        Date startDate = Date.valueOf(LocalDate.now().withDayOfMonth(1));

        // ---------------------------------------------------------
        // ΒΗΜΑ 2: ΠΡΟΣΛΗΨΕΙΣ (Hiring)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 1: Προσλήψεις Προσωπικού ---");

        // Α. Μόνιμος Καθηγητής (ID: 101)
        manager.hirePermanentTeaching(
                101, "Γιώργος Παπαδόπουλος", "Κνωσσού 50", "GR101", "6971234567",
                true, "Piraeus Bank", "Computer Science", startDate, 10, 200.0
        );

        // Β. Μόνιμος Διοικητικός (ID: 102)
        manager.hirePermanentAdministrative(
                102, "Μαρία Οικονόμου", "Εθνικής Αντιστάσεως 10", "GR102", "6981234567",
                false, "Alpha Bank", "Mathematics", startDate, 2
        );

        // Γ. Συμβασιούχος Καθηγητής (ID: 103)
        manager.hireContractorTeaching(
                103, "Κώστας Συμβασιούχος", "Λεωφόρος Ικάρου 5", "GR103", "6991234567",
                true, "Eurobank", "Physics", startDate, 50.0
        );
        // Δημιουργία Σύμβασης για τον 103
        manager.renewContract(103, startDate, Date.valueOf(LocalDate.now().plusMonths(6)), 1200.00);

        // Δ. Συμβασιούχος Διοικητικός (ID: 104)
        manager.hireContractorAdministrative(
                104, "Ελένη Δημητρίου", "Πλατεία Ελευθερίας 1", "GR104", "6901234567",
                false, "National Bank", "Computer Science", startDate
        );
        // Δημιουργία Σύμβασης για τον 104
        manager.renewContract(104, startDate, Date.valueOf(LocalDate.now().plusMonths(12)), 900.00);

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 3: ΤΡΟΠΟΠΟΙΗΣΕΙΣ (Updates & Changes)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 2: Αλλαγές Στοιχείων ---");

        // Προσθήκη Παιδιών
        manager.addChild(101, "Νικολάκης", 5);
        manager.addChild(101, "Ελενίτσα", 8);

        // Αλλαγή Διεύθυνσης
        manager.updateEmployeeDetails(102, "Νέα Διεύθυνση 100", "6989999999", "GR102-NEW", "Alpha Bank", true);

        // Αύξηση Βασικού Μισθού (Update SalaryRates)
        SalaryRateDAO salaryDAO = new SalaryRateDAO();
        salaryDAO.UpdateBaseSalary("PERMANENT_TEACHING", 1500.00);

        // Αύξηση Ειδικού Επιδόματος Έρευνας
        manager.updateResearchAllowance(101, 350.0);

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 4: ΜΙΣΘΟΔΟΣΙΑ (Payroll Execution)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 3: Εκτέλεση Μισθοδοσίας ---");

        manager.ExecutePayroll();

        System.out.println("\n");


        // ---------------------------------------------------------
        // ΒΗΜΑ 5: ΑΝΑΦΟΡΕΣ (Reporting)
        // ---------------------------------------------------------
        System.out.println("--- ΦΑΣΗ 4: Reports & Statistics ---");

        System.out.println(">> Ιστορικό Πληρωμών Υπαλλήλου 101:");
        for (String record : reportDAO.getPaymentHistory(101)) {
            System.out.println(record);
        }

        System.out.println("\n>> Στατιστικά Μισθών ανά Κατηγορία:");
        Map<String, String> stats = reportDAO.getSalaryStatsPerRole();
        for (String role : stats.keySet()) {
            System.out.println(role + ": " + stats.get(role));
        }

        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        double totalCost = reportDAO.getTotalPayrollCost(currentMonth, currentYear);
        System.out.println("\n>> Συνολικό Κόστος Μισθοδοσίας (" + currentMonth + "/" + currentYear + "): " + totalCost + "€");

        System.out.println("\n>> Ανάλυση Κόστους ανά Ρόλο:");
        Map<String, Double> costPerRole = reportDAO.getTotalCostPerRole(currentMonth, currentYear);
        for (String role : costPerRole.keySet()) {
            System.out.println(role + ": " + costPerRole.get(role) + "€");
        }


        // ---------------------------------------------------------
        // ΒΗΜΑ 6: ΑΠΟΛΥΣΗ (Firing)
        // ---------------------------------------------------------
        System.out.println("\n--- ΦΑΣΗ 5: Απόλυση Υπαλλήλου ---");
        manager.fireEmployee(104);

        System.out.println("\n=== ΤΕΛΟΣ ΠΡΟΣΟΜΟΙΩΣΗΣ ===");
    }
}