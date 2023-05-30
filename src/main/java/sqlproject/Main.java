package sqlproject;

public class Main {
    public static void main(String[] args) {
        DatabaseUtil databaseConnection = new DatabaseUtil();
        DataAccess databaseAccess = new DataAccess(databaseConnection);
        new MainGUI(databaseConnection, databaseAccess);
    }
}
