package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ItemWiseReport extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ItemWiseReport() {
        setTitle("Item-Wise Sales Report");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel lblTitle = new JLabel("Item-Wise Sales Breakdown", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"Medicine Name", "Total Quantity Sold", "Total Revenue (R)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        mainPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        add(mainPanel);

        loadItemWiseData();
    }

    private void loadItemWiseData() {
        tableModel.setRowCount(0);

        // SQL Query joining sale_items with medicines using correct column names
        String query = "SELECT m.name AS medicine_name, " +
                "SUM(si.quantity_sold) AS total_qty, " +
                "SUM(si.quantity_sold * si.price_at_sale) AS total_revenue " +
                "FROM sale_items si " +
                "JOIN medicines m ON si.medicine_id = m.id " +
                "GROUP BY m.name " +
                "ORDER BY total_revenue DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                String medicineName = rs.getString("medicine_name");
                int totalQuantity = rs.getInt("total_qty");
                double totalRevenue = rs.getDouble("total_revenue");

                tableModel.addRow(new Object[]{
                        medicineName,
                        totalQuantity,
                        String.format("R %.2f", totalRevenue)
                });
            }

            if (!hasRecords) {
                JOptionPane.showMessageDialog(this, "No sales recorded yet.", "Report Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating item-wise report: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ItemWiseReport().setVisible(true));
    }
}