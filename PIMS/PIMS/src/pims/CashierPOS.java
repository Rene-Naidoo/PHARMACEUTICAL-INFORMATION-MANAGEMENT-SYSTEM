package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CashierPOS extends JPanel {

    private JComboBox<String> cmbMedicines;
    private JTextField txtQuantity;
    private JTextField txtUnitPrice;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel lblGrandTotal;
    private double grandTotal = 0.0;

    public CashierPOS() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // Top Selection Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Item to Cart"));
        inputPanel.setOpaque(false);

        cmbMedicines = new JComboBox<>();
        txtQuantity = new JTextField("1");
        txtUnitPrice = new JTextField("0.00");
        txtUnitPrice.setEditable(false);

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setBackground(new Color(46, 204, 113));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setFocusPainted(false);

        inputPanel.add(new JLabel("Select Medicine:"));
        inputPanel.add(cmbMedicines);
        inputPanel.add(new JLabel("Unit Price (R):"));
        inputPanel.add(txtUnitPrice);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(txtQuantity);
        inputPanel.add(new JLabel("")); // Spacer
        inputPanel.add(btnAddToCart);

        add(inputPanel, BorderLayout.NORTH);

        // Center Table Panel
        String[] columnNames = {"Medicine Name", "Quantity", "Unit Price (R)", "Total Price (R)"};
        cartTableModel = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(25);
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        lblGrandTotal = new JLabel("Grand Total: R 0.00");
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setOpaque(false);

        JButton btnRemoveItem = new JButton("Remove Selected");
        btnRemoveItem.setBackground(new Color(231, 76, 60));
        btnRemoveItem.setForeground(Color.WHITE);

        JButton btnCheckout = new JButton("Complete Checkout");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(41, 128, 185));
        btnCheckout.setForeground(Color.WHITE);

        buttonGroup.add(btnRemoveItem);
        buttonGroup.add(btnCheckout);

        bottomPanel.add(lblGrandTotal, BorderLayout.WEST);
        bottomPanel.add(buttonGroup, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load medicine options
        loadMedicineDropdown();

        // Listeners
        cmbMedicines.addActionListener(e -> updateUnitPrice());
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveItem.addActionListener(e -> removeFromCart());
        btnCheckout.addActionListener(e -> processCheckout());
    }

    private void loadMedicineDropdown() {
        cmbMedicines.removeAllItems();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM medicines WHERE quantity > 0")) {

            while (rs.next()) {
                cmbMedicines.addItem(rs.getString("name"));
            }
            if (cmbMedicines.getItemCount() > 0) {
                updateUnitPrice();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading medicines: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUnitPrice() {
        String selectedMed = (String) cmbMedicines.getSelectedItem();
        if (selectedMed == null) return;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT price FROM medicines WHERE name = ?")) {

            stmt.setString(1, selectedMed);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("price");
                txtUnitPrice.setText(String.format("%.2f", price));
            }
        } catch (Exception e) {
            txtUnitPrice.setText("0.00");
        }
    }

    private void addToCart() {
        String medName = (String) cmbMedicines.getSelectedItem();
        if (medName == null) return;

        try {
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            String priceText = txtUnitPrice.getText().trim().replace(",", ".").replace("R", "").trim();
            double unitPrice = Double.parseDouble(priceText);

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double lineTotal = qty * unitPrice;
            cartTableModel.addRow(new Object[]{medName, qty, String.format("%.2f", unitPrice), String.format("%.2f", lineTotal)});

            grandTotal += lineTotal;
            lblGrandTotal.setText(String.format("Grand Total: R %.2f", grandTotal));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric quantity.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            double rowTotal = Double.parseDouble(cartTableModel.getValueAt(selectedRow, 3).toString().replace(",", "."));
            grandTotal -= rowTotal;
            if (grandTotal < 0) grandTotal = 0.0;
            lblGrandTotal.setText(String.format("Grand Total: R %.2f", grandTotal));
            cartTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item from the cart to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void processCheckout() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin database transaction

            // 1. Fetch valid user_id
            int validUserId = 1;
            try (Statement userStmt = conn.createStatement();
                 ResultSet userRs = userStmt.executeQuery("SELECT user_id FROM users LIMIT 1")) {
                if (userRs.next()) {
                    validUserId = userRs.getInt(1);
                }
            } catch (Exception e) {
                validUserId = 1;
            }

            // 2. Insert into sales header table
            String insertSaleSQL = "INSERT INTO sales (sale_date, total_amount, user_id) VALUES (NOW(), ?, ?)";
            int saleId = -1;

            try (PreparedStatement stmtSale = conn.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmtSale.setDouble(1, grandTotal);
                stmtSale.setInt(2, validUserId);
                stmtSale.executeUpdate();

                ResultSet rsKeys = stmtSale.getGeneratedKeys();
                if (rsKeys.next()) {
                    saleId = rsKeys.getInt(1);
                }
            }

            // 3. Insert items into sale_items and deduct stock safely
            String insertItemSQL = "INSERT INTO sale_items (sale_id, medicine_id, quantity_sold, price_at_sale) VALUES (?, (SELECT id FROM medicines WHERE name = ? LIMIT 1), ?, ?)";
            String updateStockSQL = "UPDATE medicines SET quantity = GREATEST(0, quantity - ?) WHERE name = ?";

            try (PreparedStatement stmtItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement stmtStock = conn.prepareStatement(updateStockSQL)) {

                for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                    String medName = cartTableModel.getValueAt(i, 0).toString();
                    int qty = Integer.parseInt(cartTableModel.getValueAt(i, 1).toString());
                    double unitPrice = Double.parseDouble(cartTableModel.getValueAt(i, 2).toString().replace(",", ".").replace("R", "").trim());

                    // Record line item
                    stmtItem.setInt(1, saleId);
                    stmtItem.setString(2, medName);
                    stmtItem.setInt(3, qty);
                    stmtItem.setDouble(4, unitPrice);
                    stmtItem.executeUpdate();

                    // Update stock
                    stmtStock.setInt(1, qty);
                    stmtStock.setString(2, medName);
                    stmtStock.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction to MySQL
            JOptionPane.showMessageDialog(this, "Checkout complete! Stock updated & transaction recorded.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Reset Cart UI
            cartTableModel.setRowCount(0);
            grandTotal = 0.0;
            lblGrandTotal.setText("Grand Total: R 0.00");
            loadMedicineDropdown();

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(this, "Checkout failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }
}