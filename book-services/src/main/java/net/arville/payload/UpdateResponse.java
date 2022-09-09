package net.arville.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.arville.model.Book;

import java.time.LocalDateTime;

public class UpdateResponse {

    private String bookNameOld;
    private String bookNameNew;
    private String authorOld;
    private String authorNew;
    private Integer priceOld;
    private Integer priceNew;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public UpdateResponse(Book oldBook, Book updatedBook, LocalDateTime updatedAt) {
        this.bookNameOld = oldBook.getBookName();
        this.bookNameNew = updatedBook.getBookName();
        this.authorOld = oldBook.getAuthor();
        this.authorNew = updatedBook.getAuthor();
        this.priceOld = oldBook.getPrice();
        this.priceNew = updatedBook.getPrice();
        this.updatedAt = updatedAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
