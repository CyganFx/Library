package Model;

import java.util.Objects;

public class Reader {
    private int readerId;
    private String username;
    private String address;
    private String phone;
    private int borrowedAmount;
    private int borrowedBookId;
    private String password;

    public Reader() {

    }

    public Reader(int readerId, String username, String address, String phone, int borrowedAmount, int borrowedBookId, String password) {
        this.readerId = readerId;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.borrowedAmount = borrowedAmount;
        this.borrowedBookId = borrowedBookId;
        this.password = password;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBorrowedAmount() {
        return borrowedAmount;
    }

    public void setBorrowedAmount(int borrowedAmount) {
        this.borrowedAmount = borrowedAmount;
    }

    public int getBorrowedBookId() {
        return borrowedBookId;
    }

    public void setBorrowedBookId(int borrowedBookId) {
        this.borrowedBookId = borrowedBookId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return readerId == reader.readerId &&
                borrowedAmount == reader.borrowedAmount &&
                Objects.equals(username, reader.username) &&
                Objects.equals(address, reader.address) &&
                Objects.equals(phone, reader.phone) &&
                Objects.equals(borrowedBookId, reader.borrowedBookId) &&
                Objects.equals(password, reader.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerId, username, address, phone, borrowedAmount, borrowedBookId, password);
    }
}
