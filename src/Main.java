import database.DatabaseService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseService.createConnection();
        deleteInvoiceAndItem(connection);
    }

    static void deleteInvoiceAndItem(Connection connection) throws SQLException {

        try {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();

            String deleteItemsQuery = "DELETE FROM Stavka WHERE RacunID=43659";
            stmt.executeUpdate(deleteItemsQuery);
            String deleteInvoiceQuery = "DELETE FROM Racun WHERE IDRacun=43659";
            stmt.executeUpdate(deleteInvoiceQuery);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
        }
    }
}
