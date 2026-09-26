package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageSuppliers extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ManageSuppliers() {
        setTitle("PIMS - Manage Suppliers");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Suppliers List", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Supplier Name", "Contact", "Address"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadSupplierData();
    }

    private void loadSupplierData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Index 1 = supplier_id, Index 2 = supplier name, Index 3 = contact, Index 4 = address
                model.addRow(new Object[]{
                        rs.getObject(1),
                        rs.getObject(2),
                        rs.getObject(3),
                        rs.getObject(4)
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}