package sqlproject;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

import java.util.ArrayList;

import java.awt.event.*;

import java.sql.SQLException;

public class UpdateLoanGUI extends JFrame implements ActionListener{
    
    JButton changeButton;
    DataAccess databaseAccess;
    JComboBox<Integer> loanNumber;

    UpdateLoanGUI(DataAccess databaseAccess) {
        this.databaseAccess = databaseAccess;

        /* 
         * Setting up JFrame parameters, title and color
         * before adding other GUI components.
        */
        this.setTitle("Update Loan Payment For Database Row");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 300);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(255,255,255));
        this.setLayout(null);
        
        /*
         * Creating a button.
         */
        Border border = BorderFactory.createLineBorder(Color.black, 3);

        changeButton = new JButton();
        changeButton.setText("Change loan");
        changeButton.setBounds(365, 200, 120, 50);
        changeButton.addActionListener(this);
        changeButton.setFocusable(false);
        changeButton.setFont(new Font("Times new Roman", Font.BOLD, 10));
        changeButton.setBackground(Color.lightGray);
        changeButton.setBorder(border);
        this.add(changeButton);

        /*
         * Inserting JComboBox object.
         */
        ArrayList<Integer> loanNumbers = null;
        try {
            loanNumbers = databaseAccess.getUnpaidLoanNumbers();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        int arraySize = loanNumbers.size();
        loanNumber = new JComboBox<>(loanNumbers.toArray(new Integer[arraySize]));
        loanNumber.addActionListener(this);
        loanNumber.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        loanNumber.setBounds(10, 10, 140, 30);
        this.add(loanNumber);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                MainGUI.windowOpened = false;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeButton) {
            int loanNr = (Integer) loanNumber.getSelectedItem();
            try {
                databaseAccess.changeLoanPayment(loanNr);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            this.dispose();
        }
    }
}

