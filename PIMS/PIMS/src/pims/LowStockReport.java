package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LowStockReport extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public LowStockReport() {
        setTitle("PIMS - Low Stock Report");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Low Stock Inventory Alert", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Medicine Name", "Quantity", "Reorder Level"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLowStockData();
    }

    private void loadLowStockData() {
        model.setRowCount(0);
        String sql = "SELECT id, name, quantity, reorder_level FROM medicines WHERE quantity <= reorder_level";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("reorder_level")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading low stock report: " + e.getMessage());
        }
    }
}