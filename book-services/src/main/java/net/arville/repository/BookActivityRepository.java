package net.arville.repository;

import net.arville.model.BookActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookActivityRepository extends JpaRepository<BookActivity, Long> {
    
}
