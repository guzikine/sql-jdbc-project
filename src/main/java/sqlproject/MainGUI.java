package sqlproject;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;

import javax.swing.border.Border;

import java.sql.SQLException;

public class MainGUI extends JFrame implements ActionListener, WindowListener {
    
    JButton createLoanButton, updateLoanButton;
    protected static boolean windowOpened;
    DatabaseUtil databaseConnection;
    DataAccess databaseAccess;

    MainGUI (DatabaseUtil databaseConnection, DataAccess databaseAccess) {
        windowOpened = false;
        this.databaseConnection = databaseConnection;
        this.databaseAccess = databaseAccess;

        /* 
         * Setting up JFrame parameters, title and color
         * before adding other GUI components.
        */
        this.setTitle("JDBC PostgreSQL Project");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 300);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(255,255,255));
        this.setLayout(null);
        
        /*
         * Adding a JLabel to the configured JFrame.
         */
        Border border = BorderFactory.createLineBorder(Color.black, 3);
        JLabel bankLabel = new JLabel();

        bankLabel.setText("BANK MANAGEMENT");
        bankLabel.setFont(new Font("Times new Roman", Font.PLAIN, 18));
        bankLabel.setHorizontalTextPosition(JLabel.CENTER);
        bankLabel.setVerticalTextPosition(JLabel.CENTER);
        bankLabel.setBorder(border);
        bankLabel.setBounds(150, 5, 200, 50);

        this.add(bankLabel);

        /*
         * Creating the necessary button components
         * for CRUD operations.
         */
        createLoanButton = new JButton();
        updateLoanButton = new JButton();

        createLoanButton.setBounds(80, 80, 130, 50);
        updateLoanButton.setBounds(290, 80, 130, 50);

        createLoanButton.addActionListener(this);
        updateLoanButton.addActionListener(this);

        createLoanButton.setText("Insert New Loan");
        updateLoanButton.setText("Set Loan Payment");
        
        createLoanButton.setFocusable(false);
        updateLoanButton.setFocusable(false);

        createLoanButton.setFont(new Font("Times new Roman", Font.BOLD, 10));
        updateLoanButton.setFont(new Font("Times new Roman", Font.BOLD, 10));

        createLoanButton.setBackground(Color.lightGray);
        updateLoanButton.setBackground(Color.lightGray);

        createLoanButton.setBorder(border);
        updateLoanButton.setBorder(border);

        this.add(createLoanButton);
        this.add(updateLoanButton);
    }

    /*
    * Overriding the ActionListener event.
    */
    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getSource() == createLoanButton && !windowOpened) {
                new InsertLoanGUI(databaseAccess);
                windowOpened = true;
            }

            if(e.getSource() == updateLoanButton && !windowOpened) {
                new UpdateLoanGUI(databaseAccess);
                windowOpened = true;
            }
        }

    /*
    * Overriding the WindowListener, if the main
    * GUI windows is closed, then the database connection
    * is closed.
    */
    @Override
    public void windowClosed(WindowEvent e) {
        try {
            databaseConnection.closeDatabase();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Empty implementations for other WindowListener methods
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}

