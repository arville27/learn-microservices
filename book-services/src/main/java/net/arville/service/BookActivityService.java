package net.arville.service;

import net.arville.model.BookActivity;
import net.arville.repository.BookActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookActivityService {

    private final BookActivityRepository bookActivityRepository;

    public BookActivityService(BookActivityRepository bookActivityRepository) {
        this.bookActivityRepository = bookActivityRepository;
    }

    public List<BookActivity> getAllBookActivity() {
        return bookActivityRepository.findAll();
    }
}
