package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Book implements Serializable, Comparator<Book>, Comparable<Book> {
    private static final long serialVersionUID = 668812036984863675L;
    private int id;
    private String name;
    private String author;
    private int countOfCopies;
    private String imageURL;
    private String isbn;

    public Book() {

    }

    public Book(int id, String name, String author, int countOfCopies, String imageURL, String isbn) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.countOfCopies = countOfCopies;
        this.imageURL = imageURL;
        this.isbn = isbn;
    }

    public Book(String[] bookFields) {
        if (bookFields.length == 6) {
            this.id = Integer.parseInt(bookFields[0]);
            this.name = bookFields[1];
            this.author = bookFields[2];
            this.countOfCopies = Integer.parseInt(bookFields[3]);
            this.imageURL = bookFields[4];
            this.isbn = bookFields[5];
        }
    }

    public Book(int id) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCountOfCopies() {
        return countOfCopies;
    }

    public void setCountOfCopies(int countOfCopies) {
        this.countOfCopies = countOfCopies;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public ArrayList<Book> lowToHigh(ArrayList<Book> list) {
        Collections.sort(list);
        return list;
    }

    public ArrayList<Book> highToLow(ArrayList<Book> list) {
        list.sort(Collections.reverseOrder());
        return list;
    }

    @Override
    public int compareTo(Book o) {
        return this.id - o.getId();
    }

    @Override
    public int compare(Book o1, Book o2) {
        return o1.getId() - o2.getId();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", countOfCopies=" + countOfCopies +
                ", imageURL='" + imageURL + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
