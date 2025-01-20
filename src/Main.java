import database.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Connection connection = DatabaseService.createConnection();
            System.out.println("Select 1.Add Author 2.Add Book to Author 3.Print books by author 4.Update book title 5. Print all authors with no books  0. Exit");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    addAuthor(connection);
                    break;
                case "2":
                    addBook(connection);
                    break;
                case "3":
                    getAllBooksByAuthor(connection);
                    break;
                case "4":
                    updateBookTitleById(connection);
                    break;
                case "5":
                    getAuthorsWithNoBooks(connection);
                    break;
                case "0":
                    System.out.println("Exiting the program.");
                    return;
            }

        }
    }

    static void addAuthor(Connection connection) {
        List<Author> authorList = new ArrayList<>();
        authorList.add((new Author("Ivano", "Horvat", "Croatian")));
        authorList.add((new Author("Ana", "Kovac", "Serbian")));
        authorList.add((new Author("Marko", "Maric", "Bosnian")));
        authorList.add((new Author("Petar", "Petrovic", "Montenegrin")));
        authorList.add((new Author("Jelena", "Nikolic", "Macedonian")));

        try {
            String insertAuthorQuery = "INSERT INTO Autor(Ime, Prezime, Nacionalnost)Values(?, ?, ?)";
            PreparedStatement insertAuthorStmt = connection.prepareStatement(insertAuthorQuery);

            for (Author author : authorList) {
                insertAuthorStmt.setString(1, author.getFirstName());
                insertAuthorStmt.setString(2, author.getLastName());
                insertAuthorStmt.setString(3, author.getNationality());
                insertAuthorStmt.executeUpdate();
            }
            System.out.println("Success");
            insertAuthorStmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addBook(Connection connection) {
        List<Book> bookList = new ArrayList<>();
        bookList.add((new Book("Prva knjiga Ivana", 2001, 1)));
        bookList.add((new Book("Druga knjiga Ivana", 2002, 1)));

        bookList.add((new Book("Prva knjiga Ane", 2022, 2)));
        bookList.add((new Book("Druga knjiga Ane", 2023, 2)));

        bookList.add((new Book("Prva knjiga Marka", 2015, 3)));
        bookList.add((new Book("Druga knjiga Marka", 2016, 3)));

        bookList.add((new Book("Prva knjiga Petra", 2002, 4)));
        bookList.add((new Book("Druga knjiga Petra", 2005, 4)));

        bookList.add((new Book("Prva knjiga Jelene", 2017, 5)));
        bookList.add((new Book("Druga knjiga Jelene", 2018, 5)));


        try {
            String insertBookQuery = "INSERT INTO Knjiga(Naslov,GodinaIzdanja,AutorId)VALUES(?, ?, ?)";
            PreparedStatement insertABookStmt = connection.prepareStatement(insertBookQuery);
            for (Book book : bookList) {
                insertABookStmt.setString(1,book.getTitle());
                insertABookStmt.setInt(2,book.getPublishYear());
                insertABookStmt.setInt(3,book.getAuthorId());
                insertABookStmt.executeUpdate();
            }
            System.out.println("success");
            insertABookStmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getAllBooksByAuthor(Connection connection){
        try {
            String bookQuery = "SELECT * FROM Knjiga WHERE AutoRId=(SELECT IDAutor FROM Autor WHERE Prezime=?)";
            PreparedStatement bookStmt = connection.prepareStatement(bookQuery);
            bookStmt.setString(1, "Horvat");
            ResultSet allBooksByAuthorResultSet= bookStmt.executeQuery();
            System.out.println("Sve knjige autora Horvata:");
            while (allBooksByAuthorResultSet.next()){
                System.out.println(allBooksByAuthorResultSet.getString("Naslov"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void updateBookTitleById(Connection connection){
        Scanner scanner = new Scanner(System.in);
        String updateBookTitleByIdQuery = "UPDATE Knjiga SET Naslov=? WHERE IDKnjiga=1";
        System.out.println("Enter new title:");
        String newTitle = scanner.nextLine();
        try {
            PreparedStatement updateBookTitleByIdStmt = connection.prepareStatement(updateBookTitleByIdQuery);
            updateBookTitleByIdStmt.setString(1,newTitle);
            updateBookTitleByIdStmt.executeUpdate();
            System.out.println("Book title was updated");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    static void getAuthorsWithNoBooks(Connection connection){

        String authorsWithNoBooksQuery = "SELECT Autor.IDAutor, Autor.Ime, Autor.Prezime FROM Autor " +
                "LEFT JOIN Knjiga ON Autor.IDAutor = Knjiga.AutorId " +
                "WHERE Knjiga.IDKnjiga IS NULL";
        try {
            PreparedStatement authorsWithNoBooksStmt = connection.prepareStatement(authorsWithNoBooksQuery);
            ResultSet resultSet = authorsWithNoBooksStmt.executeQuery();

            while (resultSet.next()) {
                int authorId = resultSet.getInt("IDAutor");
                String firstName = resultSet.getString("Ime");
                String lastName = resultSet.getString("Prezime");
                System.out.println("Autor bez knjiga: " + firstName + " " + lastName + " (ID: " + authorId + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}