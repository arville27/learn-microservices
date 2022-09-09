package net.arville.repository;

import net.arville.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    @Query("SELECT new Book(b.id, b.bookName, b.author, b.price) FROM Book b WHERE b.id = :id")
    Optional<Book> findBookWithoutDate(@Param("id") Long id);
}
