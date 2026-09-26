package pims;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Pharmacy Information Management System - Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Container Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Admin Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new Login().setVisible(true);
                this.dispose();
            }
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Grid Layout for Action Buttons
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        gridPanel.setOpaque(false);

        // Management Module Buttons
        JButton btnManageUsers = createDashboardButton("Manage Users", new Color(52, 152, 219));
        JButton btnManageMedicines = createDashboardButton("Manage Medicines", new Color(46, 204, 113));
        JButton btnManageSuppliers = createDashboardButton("Manage Suppliers", new Color(155, 89, 182));

        // Report Module Buttons (All 4 Required Reports)
        JButton btnSalesReport = createDashboardButton("Sales Report", new Color(230, 126, 34));
        JButton btnItemWiseReport = createDashboardButton("Item-Wise Report", new Color(241, 196, 15));
        JButton btnLowStockReport = createDashboardButton("Low Stock Report", new Color(231, 76, 60));
        JButton btnExpiryReport = createDashboardButton("Expiry Report", new Color(52, 73, 94));

        // POS Launcher Button
        JButton btnOpenPOS = createDashboardButton("Cashier POS", new Color(26, 188, 156));

        // Action Listeners for Management Modules
        btnManageUsers.addActionListener(e -> new ManageUsers().setVisible(true));
        btnManageMedicines.addActionListener(e -> new ManageMedicines().setVisible(true));
        btnManageSuppliers.addActionListener(e -> new ManageSuppliers().setVisible(true));

        // Action Listeners for All 4 Reports
        btnSalesReport.addActionListener(e -> new SalesReport().setVisible(true));
        btnItemWiseReport.addActionListener(e -> new ItemWiseReport().setVisible(true));
        btnLowStockReport.addActionListener(e -> new LowStockReport().setVisible(true));
        btnExpiryReport.addActionListener(e -> new ExpiryReport().setVisible(true));

        // Action Listener for POS Terminal
        btnOpenPOS.addActionListener(e -> {
            JFrame posFrame = new JFrame("POS Terminal");
            posFrame.setSize(1000, 650);
            posFrame.setLocationRelativeTo(null);
            posFrame.add(new CashierPOS());
            posFrame.setVisible(true);
        });

        // Add Buttons to Grid
        gridPanel.add(btnManageUsers);
        gridPanel.add(btnManageMedicines);
        gridPanel.add(btnManageSuppliers);
        gridPanel.add(btnSalesReport);
        gridPanel.add(btnItemWiseReport);
        gridPanel.add(btnLowStockReport);
        gridPanel.add(btnExpiryReport);
        gridPanel.add(btnOpenPOS);

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Footer Section
        JLabel lblFooter = new JLabel("Pharmacy Management System © 2026", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(Color.GRAY);
        mainPanel.add(lblFooter, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Helper method to generate styled buttons
    private JButton createDashboardButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}