/*
 * This class is used to perform CRUD operations
 * with the connected database.
 */

package sqlproject;

import java.sql.*;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.*;

public class DataAccess {

    Connection connection;
    Statement statement;

    DataAccess(DatabaseUtil databaseConnection) {
        connection = databaseConnection.getConnection();
        statement = databaseConnection.getStatement();
    }

    // For getting personal ids from the Users table.
    protected ArrayList<Long> getPersonalIds() throws SQLException {
        String personalIdQuery = "SELECT personal_id FROM Users";
        ResultSet personalIdResultSet = statement.executeQuery(personalIdQuery);
        ArrayList<Long> personalIds = new ArrayList<>();
        
        while (personalIdResultSet.next()) {
            Long personalId = personalIdResultSet.getLong("personal_id");
            personalIds.add(personalId);
        }

        return personalIds;
    }

    // For getting loan numbers from the Loan table. 
    protected ArrayList<Integer> getLoanNumbers() throws SQLException {
        String loanNumberQuery = "SELECT loan_number FROM Loan";
        ResultSet loanNumberResultSet = statement.executeQuery(loanNumberQuery);
        ArrayList<Integer> loanNumbers = new ArrayList<>();

        while (loanNumberResultSet.next()) {
            Integer loanNumber = loanNumberResultSet.getInt("loan_number");
            loanNumbers.add(loanNumber);
        }

        return loanNumbers;
    }

    // For inserting a new row into the Loan table. 
    protected void changeLoanPayment(int loanNumber) throws SQLException {
        String updatePaidSentence = "UPDATE Loan SET paid = 'yes' WHERE loan_number = ?";
        PreparedStatement updatePaidStmt = connection.prepareStatement(updatePaidSentence);
        updatePaidStmt.setInt(1, loanNumber);
        updatePaidStmt.executeUpdate();
        updatePaidStmt.close();
    }

    // For changing the table's Loan column paid to 'yes'.
    protected void insertLoan(Long personalId, 
                                int loanNumber,
                                int loanAmount, 
                                float interestRate, 
                                String deadlineDate, 
                                int paymentIntervals, 
                                String paid) throws SQLException {
        String insertLoanSentence = "INSERT INTO Loan (loan_number, " + 
                                "loan_amount, interest_rate, loan_initial_date, " + 
                                "loan_deadline_date, payment_intervals, paid) " + 
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertConnectionSentence = "INSERT INTO Connection (personal_id, loan_number) VALUES (?, ?)";
        PreparedStatement insertLoanStmt = connection.prepareStatement(insertLoanSentence);
        PreparedStatement insertConnectionStmt = connection.prepareStatement(insertConnectionSentence);

        insertLoanStmt.setInt(1, loanNumber);
        insertLoanStmt.setInt(2, loanAmount);
        insertLoanStmt.setFloat(3, interestRate);
        insertLoanStmt.setDate(4, Date.valueOf(LocalDate.now()));
        insertLoanStmt.setDate(5, Date.valueOf(LocalDate.parse(deadlineDate)));
        insertLoanStmt.setInt(6, paymentIntervals);
        insertLoanStmt.setString(7, paid);

        insertConnectionStmt.setLong(1, personalId);
        insertConnectionStmt.setInt(2, loanNumber);

        try {
            connection.setAutoCommit(false);
            
            insertLoanStmt.executeUpdate();
            insertConnectionStmt.executeUpdate();

            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            connection.rollback();
            connection.setAutoCommit(true);
        }

        insertLoanStmt.close();
        insertConnectionStmt.close();
    }

    // For getting the complete Loan table record.
    protected ArrayList<String> getLoanRows() throws SQLException {
        String loanRowQuery = "SELECT * FROM Loan";
        ResultSet loanRowResultSet = statement.executeQuery(loanRowQuery);
        ArrayList<String> loanRows = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (loanRowResultSet.next()) {
            int loanNumber = loanRowResultSet.getInt("loan_Number");
            int loanAmount = loanRowResultSet.getInt("loan_amount");
            Date initialDate = loanRowResultSet.getDate("loan_initial_date");
            Date deadlineDate = loanRowResultSet.getDate("loan_deadline_date");
            String paid = loanRowResultSet.getString("paid");

            LocalDate initialDateLocal = initialDate.toLocalDate();
            LocalDate deadlineDateLocal = deadlineDate.toLocalDate();

            String formattedInitialDate = initialDateLocal.format(formatter);
            String formattedDeadlineDate = deadlineDateLocal.format(formatter);

            String concatenatedRow = loanNumber + " | " + loanAmount + " | " + formattedInitialDate 
                            + " | " + formattedDeadlineDate + " | " + paid;
            
            loanRows.add(concatenatedRow);
        }

        return loanRows;
    }

    // For getting unpaid loan numbers.
    protected ArrayList<Integer> getUnpaidLoanNumbers() throws SQLException {
        String unpaidLoanQuery = "SELECT loan_number FROM Loan WHERE paid = 'no'";
        ResultSet unpaidLoanResultSet = statement.executeQuery(unpaidLoanQuery);
        ArrayList<Integer> unpaidLoanNumbers = new ArrayList<>();
        
        while (unpaidLoanResultSet.next()) {
            Integer loanNumber = unpaidLoanResultSet.getInt("loan_number");
            unpaidLoanNumbers.add(loanNumber);
        }

        return unpaidLoanNumbers;
    }
}
