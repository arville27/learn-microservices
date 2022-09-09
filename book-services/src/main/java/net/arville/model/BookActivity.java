package net.arville.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "daniel_book_store_activity_log")
public class BookActivity {

    @Id
    @SequenceGenerator(name = "book_activity_seq", sequenceName = "book_activity_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_activity_seq")
    Long id;

    String activity;

    @Column(name = "book_name_old")
    String bookNameOld;

    @Column(name = "book_name_new")
    String bookNameNew;

    @Column(name = "author_old")
    String authorOld;

    @Column(name = "author_new")
    String authorNew;

    @Column(name = "price_old")
    Integer priceOld;

    @Column(name = "price_new")
    Integer priceNew;

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    public BookActivity() {

    }

    public Long getId() {
        return id;
    }

    public BookActivity(String activity, Book oldBook, Book newBook) {
        this.activity = activity;

        this.bookNameOld = oldBook.getBookName();
        this.authorOld = oldBook.getAuthor();
        this.priceOld = oldBook.getPrice();

        this.bookNameNew = newBook.getBookName();
        this.authorNew = newBook.getAuthor();
        this.priceNew = newBook.getPrice();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getBookNameOld() {
        return bookNameOld;
    }

    public void setBookNameOld(String bookNameOld) {
        this.bookNameOld = bookNameOld;
    }

    public String getBookNameNew() {
        return bookNameNew;
    }

    public void setBookNameNew(String bookNameNew) {
        this.bookNameNew = bookNameNew;
    }

    public String getAuthorOld() {
        return authorOld;
    }

    public void setAuthorOld(String authorOld) {
        this.authorOld = authorOld;
    }

    public String getAuthorNew() {
        return authorNew;
    }

    public void setAuthorNew(String authorNew) {
        this.authorNew = authorNew;
    }

    public Integer getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Integer priceOld) {
        this.priceOld = priceOld;
    }

    public Integer getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(Integer priceNew) {
        this.priceNew = priceNew;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookActivity(String activity, String bookNameOld, String bookNameNew, String authorOld, String authorNew, Integer priceOld, Integer priceNew) {
        this.activity = activity;
        this.bookNameOld = bookNameOld;
        this.bookNameNew = bookNameNew;
        this.authorOld = authorOld;
        this.authorNew = authorNew;
        this.priceOld = priceOld;
        this.priceNew = priceNew;
    }
}
