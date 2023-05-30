package sqlproject;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.border.Border;

import java.util.ArrayList;

public class InsertLoanGUI extends JFrame implements ActionListener{
    
    JButton insertButton;
    JTextField loanAmount, interestRate, loanDeadlineDate, paymentIntervals;
    JComboBox<Long> personalId;
    DataAccess databaseAccess;

    InsertLoanGUI(DataAccess databaseAccess) {
        this.databaseAccess = databaseAccess;

        /* 
         * Setting up JFrame parameters, title and color
         * before adding other GUI components.
        */
        this.setTitle("Insert New Loan Into Database");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 500);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(255,255,255));
        this.setLayout(null);
        
        /*
         * Defining insert button.
         */
        Border border = BorderFactory.createLineBorder(Color.black, 3);

        insertButton = new JButton();
        insertButton.setText("Insert data");
        insertButton.setBounds(365, 400, 120, 50);
        insertButton.addActionListener(this);
        insertButton.setFocusable(false);
        insertButton.setFont(new Font("Times new Roman", Font.BOLD, 10));
        insertButton.setBackground(Color.lightGray);
        insertButton.setBorder(border);
        this.add(insertButton);

        /*
         * Defining text boxes for input.
         */
        loanAmount = new JTextField();
        interestRate = new JTextField();
        loanDeadlineDate = new JTextField();
        paymentIntervals = new JTextField();

        loanAmount.setBounds(10, 10, 140, 30);
        interestRate.setBounds(10, 50, 140, 30);
        loanDeadlineDate.setBounds(10, 90, 140, 30);
        paymentIntervals.setBounds(10, 130, 140, 30);

        loanAmount.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        interestRate.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        loanDeadlineDate.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        paymentIntervals.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        
        ArrayList<Long> personalIdArray = null;
        try {
            personalIdArray = databaseAccess.getPersonalIds();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        int arraySize = personalIdArray.size();
        personalId = new JComboBox<>(personalIdArray.toArray(new Long[arraySize]));
        personalId.addActionListener(this);
        personalId.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        personalId.setBounds(10, 170, 140, 30);

        this.add(loanAmount);
        this.add(interestRate);
        this.add(loanDeadlineDate);
        this.add(paymentIntervals);
        this.add(personalId);

        /*
         * Creating JPanels for text.
         */
        JLabel loanAmountLabel = new JLabel();
        JLabel interestRateLabel = new JLabel();
        JLabel loanDeadlineDateLabel = new JLabel();
        JLabel paymentIntervalsLabel = new JLabel();
        JLabel personalIdLabel = new JLabel();

        loanAmountLabel.setText("Loan Amount");
        interestRateLabel.setText("Interest Rate");
        loanDeadlineDateLabel.setText("Deadline Date");
        paymentIntervalsLabel.setText("Payment Interval");
        personalIdLabel.setText("User Personal Id");

        loanAmountLabel.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        interestRateLabel.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        loanDeadlineDateLabel.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        paymentIntervalsLabel.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        personalIdLabel.setFont(new Font("Times new Roman", Font.PLAIN, 10));

        loanAmountLabel.setHorizontalTextPosition(JLabel.CENTER);
        loanAmountLabel.setVerticalTextPosition(JLabel.CENTER);
        interestRateLabel.setHorizontalTextPosition(JLabel.CENTER);
        interestRateLabel.setVerticalTextPosition(JLabel.CENTER);
        loanDeadlineDateLabel.setHorizontalTextPosition(JLabel.CENTER);
        loanDeadlineDateLabel.setVerticalTextPosition(JLabel.CENTER);
        paymentIntervalsLabel.setHorizontalTextPosition(JLabel.CENTER);
        paymentIntervalsLabel.setVerticalTextPosition(JLabel.CENTER);
        personalIdLabel.setHorizontalTextPosition(JLabel.CENTER);
        personalIdLabel.setVerticalTextPosition(JLabel.CENTER);

        loanAmountLabel.setBounds(155, 10, 130, 30);
        interestRateLabel.setBounds(155, 50, 130, 30);
        loanDeadlineDateLabel.setBounds(155, 90, 130, 30);
        paymentIntervalsLabel.setBounds(155, 130, 130, 30);
        personalIdLabel.setBounds(155, 170, 130, 30);

        this.add(loanAmountLabel);
        this.add(interestRateLabel);
        this.add(loanDeadlineDateLabel);
        this.add(paymentIntervalsLabel);
        this.add(personalIdLabel);

        /*
         * Monitors the window close action, if it is triggered
         * then the windowOpened static boolean from MainGUI
         * class is set to false.
         */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                MainGUI.windowOpened = false;
                loanAmount.getText();
                personalId.getSelectedItem();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == insertButton) {
            Long selectedValue = (Long) personalId.getSelectedItem();
            String[] answers = new String[4];
            answers[0] = loanAmount.getText();
            answers[1] = interestRate.getText();
            answers[2] = loanDeadlineDate.getText();
            answers[3] = paymentIntervals.getText();
            int loanNumber = 0;
            int pass = 1;
            try {
                loanNumber = databaseAccess.getLoanNumbers().size() + 1;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            // Checks whether all lines are filled, if not throws JOptionPane
            for (int i = 0; i < answers.length; i++) {
                if (answers[i].equals("")) {
                    pass = 0;
                }
            }

            if (pass == 0) {
                JOptionPane.showMessageDialog(null, "All text spaces must be filled.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            if (pass == 1) {
                try {
                databaseAccess.insertLoan(selectedValue, 
                                    loanNumber, Integer.parseInt(answers[0]), 
                                    Float.parseFloat(answers[1]), 
                                    answers[2], 
                                    Integer.parseInt(answers[3]),
                                    "no");
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                
                this.dispose();
            }
        }
    }
}
