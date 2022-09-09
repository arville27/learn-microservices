package net.arville.repository;

import net.arville.model.UsersBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersBookRepository extends JpaRepository<UsersBook, Long>, JpaSpecificationExecutor<UsersBook> {

    List<UsersBook> findAllByUserId(Long userId);

    Optional<UsersBook> findByUserIdAndBookId(Long userId, Long bookId);

    List<UsersBook> findAllByBookId(Long bookId);
}
