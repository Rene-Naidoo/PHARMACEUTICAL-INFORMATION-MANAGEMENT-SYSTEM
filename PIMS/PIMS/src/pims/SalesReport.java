package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SalesReport extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;

    public SalesReport() {
        setTitle("General Sales Transaction Log");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel lblTitle = new JLabel("Sales Transaction Receipts", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"Sale ID", "Date & Time", "Total Amount (R)", "Processed By (User ID)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        mainPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        add(mainPanel);

        loadSalesTransactions();
    }

    private void loadSalesTransactions() {
        tableModel.setRowCount(0);

        // SQL Query reading general receipts from sales table
        String query = "SELECT sale_id, sale_date, total_amount, user_id FROM sales ORDER BY sale_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                int saleId = rs.getInt("sale_id");
                String saleDate = rs.getString("sale_date");
                double totalAmount = rs.getDouble("total_amount");
                int userId = rs.getInt("user_id");

                tableModel.addRow(new Object[]{
                        saleId,
                        saleDate,
                        String.format("R %.2f", totalAmount),
                        "User #" + userId
                });
            }

            if (!hasRecords) {
                JOptionPane.showMessageDialog(this, "No sales transactions recorded yet.", "Report Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading sales transactions: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalesReport().setVisible(true));
    }
}