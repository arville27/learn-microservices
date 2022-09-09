package net.arville.model;

import javax.persistence.*;

@Entity
@Table(name = "daniel_usersbook_store")
public class UsersBook {

    @Id
    @SequenceGenerator(name = "usersbook_id_seq", sequenceName = "usersbook_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersbook_id_seq")
    private Long id;

    private Long userId;

    private Long bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public UsersBook() {
    }
}
