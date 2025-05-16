import models.User;
import services.AuthService;
import services.ReportService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        Scanner scan = new Scanner(System.in);
        User loggedInUser = null;

        while (loggedInUser == null) {
            System.out.println("--- ReportX ---\n");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scan.nextLine();

            if (choice.equals("1")) {
                authService.register();
            } else if (choice.equals("2")) {
                loggedInUser = authService.login();
            } else if (choice.equals("3")) {
                System.out.println("Thank you for using ReportX!");
                return;
            } else {
                System.out.println("Invalid option.");
            }
        }

        System.out.println("Welcome back, " + loggedInUser.getUsername() + "!\n");

        ReportService reportService = new ReportService();

        while (loggedInUser.getRole().equals("user")) {
            System.out.println("--- User Menu ---");
            System.out.println("1. Submit Scam Report");
            System.out.println("2. View My Reports");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scan.nextLine();

            if (choice.equals("1")) {
                reportService.submitReport(loggedInUser.getId());
            } else if (choice.equals("2")) {
                reportService.viewMyReports(loggedInUser.getId());
            } else if (choice.equals("3")) {
                System.out.println("Thank you for using ReportX!");
                return;
            } else {
                System.out.println("Invalid option.\n");
            }
        }

        while (loggedInUser.getRole().equals("admin")) {
            System.out.println("--- Admin Menu ---");
            System.out.println("1. Submit Report");
            System.out.println("2. View All Reports");
            System.out.println("3. View Pending Reports");
            System.out.println("4. View Reported Reports");
            System.out.println("5. View Solved Reports");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            String choice = scan.nextLine();

            switch (choice) {
                case "1":
                    reportService.submitReport(loggedInUser.getId());
                    break;
                case "2":
                    reportService.viewReportsByStatus("all");
                    break;
                case "3":
                    reportService.viewReportsByStatus("pending");
                    break;
                case "4":
                    reportService.viewReportsByStatus("reported");
                    break;
                case "5":
                    reportService.viewReportsByStatus("solved");
                    break;
                case "6":
                    System.out.println("Exiting admin menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}