package net.arville.model.projection;

import net.arville.model.Book;

public interface BookWithoutDate {
    String getBookName();
    String getAuthor();
    Integer getPrice();

    default Book toBook(){
        Book book = new Book();
        book.setBookName(this.getBookName());
        book.setAuthor(this.getAuthor());
        book.setPrice(this.getPrice());
        return book;
    }
}
