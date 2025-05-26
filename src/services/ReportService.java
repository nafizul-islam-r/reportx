package services;

import db.DBConnection;
import models.ScamReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ReportService {
    private Scanner scanner = new Scanner(System.in);

    public void submitReport(int userId) {
        System.out.print("Scam type (Online/Offline): ");
        String scamType = scanner.nextLine();

        System.out.print("Scammer name: ");
        String scammerName = scanner.nextLine();

        System.out.print("Scammer contact/info: ");
        String scammerInfo = scanner.nextLine();

        System.out.print("Description of the scam: ");
        String description = scanner.nextLine();

        ScamReport report = new ScamReport(userId, scamType, scammerName, scammerInfo, description);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO scam_reports (user_id, scam_type, scammer_name, scammer_info, description, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, report.getUserId());
            stmt.setString(2, report.getScamType());
            stmt.setString(3, report.getScammerName());
            stmt.setString(4, report.getScammerInfo());
            stmt.setString(5, report.getDescription());
            stmt.setString(6, report.getStatus());

            stmt.executeUpdate();
            System.out.println("✅ Scam report submitted!\n");
        } catch (Exception e) {
            System.out.println("Error submitting report: " + e.getMessage() + "\n");
        }
    }

    public void viewMyReports(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM scam_reports WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("📄 Fetching your reports...");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("---------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Scam Type: " + rs.getString("scam_type"));
                System.out.println("Scammer Name: " + rs.getString("scammer_name"));
                System.out.println("Info: " + rs.getString("scammer_info"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Created At: " + rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("❌ No reports submitted yet.\n");
            }

        } catch (Exception e) {
            System.out.println("Error fetching reports: " + e.getMessage() + "\n");
        }
    }

    public void viewReportsByStatus(String status) {
        String sql = status.equals("all")
                ? "SELECT * FROM scam_reports"
                : "SELECT * FROM scam_reports WHERE status = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = status.equals("all")
                        ? conn.prepareStatement(sql)
                        : conn.prepareStatement(sql)) {

            if (!status.equals("all")) {
                stmt.setString(1, status);
            }

            ResultSet rs = stmt.executeQuery();
            Scanner scan = new Scanner(System.in);
            int count = 0;

            System.out.println("--- Reports Summary ---");
            while (rs.next()) {
                count++;
                System.out.println("ID: " + rs.getInt("id") + " | Scammer: " + rs.getString("scammer_name")
                        + " | Type: " + rs.getString("scam_type") + " | Status: " + rs.getString("status"));
            }

            if (count == 0) {
                System.out.println("No reports found.\n");
                return;
            }

            System.out.print("Enter report ID to view details or 0 to return: ");
            int reportId = Integer.parseInt(scan.nextLine());
            if (reportId != 0) {
                viewReportDetails(reportId);
            }

        } catch (Exception e) {
            System.out.println("Error viewing reports: " + e.getMessage() + "\n");
        }
    }

    public void viewReportDetails(int reportId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM scam_reports WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Report Details ---");
                System.out.println("Scammer Name: " + rs.getString("scammer_name"));
                System.out.println("Scam Type: " + rs.getString("scam_type"));
                System.out.println("Info: " + rs.getString("scammer_info"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Created At: " + rs.getTimestamp("created_at"));

                Scanner scan = new Scanner(System.in);
                System.out.println("\n--- Actions ---");
                System.out.println("1. Change Status");
                System.out.println("2. Delete Report");
                System.out.println("3. Back");
                System.out.print("Choose: ");
                String action = scan.nextLine();

                switch (action) {
                    case "1":
                        System.out.print("Enter new status (pending/reported/solved): ");
                        String newStatus = scan.nextLine();
                        updateReportStatus(reportId, newStatus);
                        break;
                    case "2":
                        deleteReport(reportId);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid option.\n");
                }
            } else {
                System.out.println("Report not found.\n");
            }

        } catch (Exception e) {
            System.out.println("Error fetching report details: " + e.getMessage() + "\n");
        }
    }

    private void updateReportStatus(int reportId, String newStatus) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE scam_reports SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, reportId);
            stmt.executeUpdate();
            System.out.println("✅ Status updated successfully.\n");
        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage() + "\n");
        }
    }

    private void deleteReport(int reportId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM scam_reports WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reportId);
            stmt.executeUpdate();
            System.out.println("🗑️ Report deleted.\n");
        } catch (Exception e) {
            System.out.println("Error deleting report: " + e.getMessage() + "\n");
        }
    }
}
