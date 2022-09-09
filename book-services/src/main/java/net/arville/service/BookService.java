package net.arville.service;

import net.arville.exception.ItemNotFoundException;
import net.arville.model.Book;
import net.arville.model.BookActivity;
import net.arville.payload.PaginationResponse;
import net.arville.repository.BookActivityRepository;
import net.arville.repository.BookRepository;
import net.arville.payload.UpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookActivityRepository bookActivityRepository;

    private final RestTemplate restTemplate;

    public BookService(BookRepository bookRepository, BookActivityRepository bookActivityRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.bookActivityRepository = bookActivityRepository;
        this.restTemplate = restTemplate;
    }

    public Book addBook(Book book) {
        bookActivityRepository.save(new BookActivity("INSERT", new Book(), book));
        return bookRepository.save(book);
    }

    public PaginationResponse getAllBookBy(Specification<Book> specsCriteria, Pageable pageable) {
        Page<Book> books = bookRepository.findAll(specsCriteria, pageable);
        if (books.getSize() == 0) {
            throw new ItemNotFoundException();
        }
        return new PaginationResponse(
                books.get().collect(Collectors.toList()),
                books.getNumber() + 1,
                books.getTotalPages(),
                books.getTotalElements());
    }

    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findBookWithoutDate(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new ItemNotFoundException();
        }
    }

    @Transactional
    public UpdateResponse updateBook(Long id, Book newBookData) {
        String newName = newBookData.getBookName();
        String newAuthor = newBookData.getAuthor();
        Integer newPrice = newBookData.getPrice();

        Book updatedBook = this.getBookById(id);

        Book beforeBook, afterBook;
        beforeBook = new Book(updatedBook);
        afterBook = new Book(updatedBook);

        if (newName != null && newName.length() > 0) {
            if (!Objects.equals(newName, updatedBook.getBookName())) {
                updatedBook.setBookName(newName);
                afterBook.setBookName(newName);
            } else {
                afterBook.setBookName("No Change");
            }
        }

        if (newAuthor != null && newAuthor.length() > 0) {
            if (!Objects.equals(newAuthor, updatedBook.getAuthor())) {
                updatedBook.setAuthor(newAuthor);
                afterBook.setAuthor(newAuthor);
            } else {
                afterBook.setAuthor("No Change");
            }
        }

        if (newPrice != null && !Objects.equals(newPrice, updatedBook.getPrice())) {
            updatedBook.setPrice(newPrice);
        }

        bookActivityRepository.save(new BookActivity("UPDATE", beforeBook, afterBook));

        return new UpdateResponse(beforeBook, afterBook, LocalDateTime.now());
    }

    public Book deleteBookById(Long id) {
        Book book = this.getBookById(id);

        // Delete all users book that have reference to this book
        try {
            String endpoint = "http://user-services/api/usersbook";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Book> request = new HttpEntity<>(book, headers);
            restTemplate.exchange(endpoint, HttpMethod.POST, request, String.class);
        } catch (HttpStatusCodeException e) {
            throw e;
        }

        bookRepository.deleteById(id);
        return book;
    }
}
