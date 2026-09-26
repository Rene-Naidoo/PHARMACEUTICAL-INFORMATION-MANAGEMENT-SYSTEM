package pims;

import javax.swing.*;
import java.awt.*;

public class Bill extends JFrame {

    public Bill(String billDetails, double totalAmount) {
        setTitle("PIMS - Transaction Receipt");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblHeader = new JLabel("PHARMACY RECEIPT", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblHeader, BorderLayout.NORTH);

        JTextArea txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReceipt.setEditable(false);
        txtReceipt.setText(billDetails + "\n----------------------------------------\n" +
                String.format("TOTAL AMOUNT: R%.2f\n", totalAmount) +
                "----------------------------------------\n" +
                "Thank you for your purchase!");

        add(new JScrollPane(txtReceipt), BorderLayout.CENTER);

        JButton btnClose = new JButton("Print / Close");
        btnClose.addActionListener(e -> this.dispose());
        add(btnClose, BorderLayout.SOUTH);
    }
}