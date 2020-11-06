package dao;

import Model.Book;
import Model.Reader;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;

public class DB {
    protected static Connection connection;
    protected static ResultSet resultSet;
    ResultSetMetaData metaData;
    protected static PreparedStatement statement;

    public static void getConnection() {
        try {
            Context initialContext = new InitialContext();
            Context envCtx = (Context) initialContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/week");
            connection = ds.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> read() {
        ArrayList<Book> bookList = new ArrayList<>();
        try {
            getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("Select*from book");
            metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();
            Book book;
            while (resultSet.next()) {
                String[] bookFields = new String[numOfColumns];
                for (int a = 1; a <= numOfColumns; a++) {
                    bookFields[a - 1] = resultSet.getObject(a).toString();
                }
                book = new Book(bookFields);
                bookList.add(book);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return bookList;
    }

    public static boolean checkReader(String username, String password) {
        boolean st = false;
        try {
            getConnection();
            statement = connection.prepareStatement("SELECT * FROM reader WHERE username=? and password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            st = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public int addBook(int id, String name, String author, int countOfCopies, String imageUrl, String isbn) {
        int added;
        try {
            getConnection();
            statement = connection.prepareStatement("Insert into book(book_id, book_name, author, countofcopies, book_url, isbn) VALUES (?,?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, author);
            statement.setInt(4, countOfCopies);
            statement.setString(5, imageUrl);
            statement.setString(6, isbn);
            added = statement.executeUpdate();
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return added;
    }

    public String addBookToReader(String username, int bookId, int countOfCopies) throws SQLException {
        String errorMessage = "ok";
        if (!checkBooksAmount(bookId, countOfCopies)) {
            errorMessage = "There is not enough books to borrow " + countOfCopies + " books with id: " + bookId;
            return errorMessage;
        }
        try {
            getConnection();
            statement = connection.prepareStatement("select * from reader where username = ? and borrowed_book_id = ?");
            statement.setString(1, username);
            statement.setInt(2, bookId);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();

            PreparedStatement preparedStatement6 = connection.prepareStatement("select countofcopies from book where book_id = ?");
            preparedStatement6.setInt(1, bookId);
            ResultSet resultSet2 = preparedStatement6.executeQuery();
            int amountOfBooksInDB = 0;
            while (resultSet2.next()) {
                amountOfBooksInDB = resultSet2.getInt("countofcopies");
            }

            PreparedStatement preparedStatement8 = connection.prepareStatement("select borrowed_amount from reader where username = ? and borrowed_book_id = ?");
            preparedStatement8.setString(1, username);
            preparedStatement8.setInt(2, bookId);
            ResultSet resultSet3 = preparedStatement8.executeQuery();
            int amountOfBooksUserHas = 0;
            while (resultSet3.next()) {
                amountOfBooksUserHas = resultSet3.getInt("borrowed_amount");
            }
            preparedStatement8.close();

            if (resultSet.next()) {
                PreparedStatement preparedStatement2 = connection.prepareStatement("update reader set borrowed_amount = ? where username = ? and borrowed_book_id = ?");
                preparedStatement2.setInt(1, amountOfBooksUserHas + countOfCopies);
                preparedStatement2.setString(2, username);
                preparedStatement2.setInt(3, bookId);
                preparedStatement2.executeUpdate();
                preparedStatement2.close();

                PreparedStatement preparedStatement5 = connection.prepareStatement("update book set countofcopies = ? where book_id = ?");
                preparedStatement5.setInt(1, amountOfBooksInDB - countOfCopies);
                preparedStatement5.setInt(2, bookId);
                preparedStatement5.executeUpdate();

                preparedStatement5.close();
                connection.close();
            } else {
                PreparedStatement preparedStatement4 = connection.prepareStatement("select reader_id from reader where username = ?");
                preparedStatement4.setString(1, username);
                resultSet = preparedStatement4.executeQuery();
                int readerId = 0;
                while (resultSet.next()) {
                    readerId = resultSet.getInt("reader_id");
                }
                PreparedStatement preparedStatement3 = connection.prepareStatement("insert into reader(reader_id, username, borrowed_book_id, borrowed_amount) values (?,?,?,?)");
                preparedStatement3.setInt(1, readerId);
                preparedStatement3.setString(2, username);
                preparedStatement3.setInt(3, bookId);
                preparedStatement3.setInt(4, countOfCopies);

                preparedStatement3.executeUpdate();
                preparedStatement3.close();
                preparedStatement4.close();

                PreparedStatement preparedStatement7 = connection.prepareStatement("update book set countofcopies = ? where book_id = ?");
                preparedStatement7.setInt(1, amountOfBooksInDB - countOfCopies);
                preparedStatement7.setInt(2, bookId);
                preparedStatement7.executeUpdate();

                preparedStatement7.close();
                connection.close();

                return errorMessage;
            }
            preparedStatement6.close();
        } catch (SQLException e) {
            System.out.println("catch clause:");
        }
        return errorMessage;
    }

    public String addReader(int id, String username, String password, String address, String phone) {
        String errorMessage = "ok";
        if (!checkReaderId(id)) {
            errorMessage = "reader_id " + id + " already exist!";
        } else {
            try {
                getConnection();
                statement = connection.prepareStatement("Insert into reader(reader_id, username, password, address, phone) VALUES (?,?,?,?,?)");
                statement.setInt(1, id);
                statement.setString(2, username);
                statement.setString(3, password);
                statement.setString(4, address);
                statement.setString(5, phone);
                int result = statement.executeUpdate();
                if (result == 0) {
                    errorMessage = "Couldn't add reader for some reason";
                }
                connection.close();
                statement.close();
            } catch (SQLException e) {
                return errorMessage;
            }
        }
        return errorMessage;
    }

    private boolean checkReaderId(int id) {
        try {
            getConnection();
            statement = connection.prepareStatement("select reader_id from reader where reader_id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkBooksAmount(int bookId, int countOfCopies) {
        try {
            getConnection();
            statement = connection.prepareStatement("select countofcopies from book where book_id = ?");
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int amount = resultSet.getInt("countofcopies");
                if (amount - countOfCopies >= 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int updateBook(int id, String name, String author, int countOfCopies, String imageURL) {
        int updated = 0;
        try {
            getConnection();
            statement = connection.prepareStatement("update book set book_name=?, author=?, countofcopies=?, book_url=? where book_id=?");
            statement.setString(1, name);
            statement.setString(2, author);
            statement.setInt(3, countOfCopies);
            statement.setString(4, imageURL);
            statement.setInt(5, id);
            updated = statement.executeUpdate();
            connection.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return updated;
    }


    public int deleteBook(String id) {
        int deleted = 0;
        try {
            getConnection();
            statement = connection.prepareStatement("Delete from book WHERE book_id =?");
            statement.setInt(1, Integer.parseInt(id));
            deleted = statement.executeUpdate();
            connection.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return deleted;
    }

    public ArrayList<Book> searchReader(String name) {
        ArrayList<Book> bookList = new ArrayList();
        try {
            getConnection();
            statement = connection.prepareStatement("Select * from book where book_name=? or author=?");
            statement.setString(1, name);
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Book book;
            while (resultSet.next()) {
                String[] bookFields = new String[numberOfColumns];
                for (int a = 1; a <= numberOfColumns; a++) {
                    bookFields[a - 1] = resultSet.getObject(a).toString();
                }
                book = new Book(bookFields);
                bookList.add(book);
            }
            resultSet.close();
            connection.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return bookList;
    }

    public Stack<Reader> getAllReaders() {
        Stack<Reader> stack = new Stack<>();
        try {
            getConnection();
            statement = connection.prepareStatement("select * from reader order by reader_id");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("reader_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                int amount = resultSet.getInt("borrowed_amount");
                int bookId = resultSet.getInt("borrowed_book_id");

                Reader r = new Reader();
                r.setReaderId(id);
                r.setUsername(username);
                r.setPassword(password);
                r.setAddress(address);
                r.setPhone(phone);
                r.setBorrowedAmount(amount);
                r.setBorrowedBookId(bookId);
                stack.push(r);
                r = null;
            }
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stack;
    }

    public int updateReader(int readerId, String username, String address, String phone, int bookAmount, int bookId) {
        int updated = 0;
        try {
            getConnection();
            statement = connection.prepareStatement(
                    "update reader set username=?, address=?, phone=?, borrowed_amount=? where reader_id = ? and borrowed_book_id = ?");
            statement.setString(1, username);
            statement.setString(2, address);
            statement.setString(3, phone);
            statement.setInt(4, bookAmount);
            statement.setInt(5, readerId);
            statement.setInt(6, bookId);
            updated = statement.executeUpdate();
            connection.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return updated;
    }

    public int deleteReader(int readerId) {
        int deleted = 0;
        if (readerId == 1) {
            return -1;
        }
        try {
            getConnection();
            statement = connection.prepareStatement("Delete from reader WHERE reader_id = ? and (borrowed_amount = 0 or borrowed_amount is null)");
            statement.setInt(1, readerId);
            deleted = statement.executeUpdate();
            connection.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return deleted;
    }

}
