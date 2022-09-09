package net.arville.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "daniel_book_store")
public class Book {
    @Id
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @JsonAlias("book_id")
    private Long id;

    @Column(name = "book_name", length = 50)
    private String bookName;

    @Column(length = 25)
    private String author;

    private Integer price;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    //    Copy constructor for activity book log
    public Book(Book original) {
        this.id = original.id;
        this.bookName = new String(original.getBookName());
        this.author = new String(original.getAuthor());
        this.price = original.getPrice();
    }

    public Book() {
    }

    public Book(Long id, String bookName, String author, Integer price) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
    }

    public Book(String bookName, String author, Integer price) {
        this.bookName = bookName;
        this.author = author;
        this.price = price;
    }
}
