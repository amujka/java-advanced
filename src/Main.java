import database.DatabaseService;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseService.createConnection();
        //deleteInvoiceAndItem(connection);
        createInvoice(connection);
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

    static void createInvoice(Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt;

            String createInvoiceQuery = "INSERT INTO Racun(Datumizdavanja,BrojRacuna,KupacID)VALUES('01-20-2025','SO12345',216)";
            stmt = connection.prepareStatement(createInvoiceQuery);
            System.out.println("Invoice added");

            String selectInvoiceIdQuery="SELECT RacunID FROM Racun WHERE BrojRacuna=SO12345";
            ResultSet rs = stmt.executeQuery(selectInvoiceIdQuery);

            String insertItemQuery ="INSERT INTO Stavka(RacunID,Kolicina,ProizvodID,CijenaPoKomadu,PopustUPostocima,UkupnaCijena)" +
                    "VALUES(?,?,?,?,?,?)";
            stmt = connection.prepareStatement(insertItemQuery);
            stmt.setInt(1,rs.getInt("IDRacun"));
            stmt.setInt(2,1);
            stmt.setInt(3, 762);
            stmt.setDouble(4, 419.4589);
            stmt.setDouble(5, 0.00);
            stmt.setDouble(6, 419.458900);
            stmt.executeUpdate();
            System.out.println("Item added");

            String getQuantityQuery = "SELECT MinimalnaKolicinaNaSkladistu FROM Proizvod WHERE IDProizvod = ?";
            stmt = connection.prepareStatement(getQuantityQuery);
            stmt.setInt(1, 762);
            rs = stmt.executeQuery();

            String updateItemQuantity = "UPDATE Proizvod" +
                    "MinimalnaKolicinaNaSkladistu=? WHERE IDProizvod=762";
            stmt = connection.prepareStatement(updateItemQuantity);
            stmt.setInt(1, rs.getInt("MinimalnaKolicinaNaSkladistu"));

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }
}
