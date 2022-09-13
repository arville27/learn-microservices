package net.arville.service;

import net.arville.enumeration.SpecificationOperation;
import net.arville.exception.ItemNotFoundException;
import net.arville.model.Book;
import net.arville.model.BookActivity;
import net.arville.payload.PaginationResponse;
import net.arville.repository.BookActivityRepository;
import net.arville.repository.BookRepository;
import net.arville.payload.UpdateResponse;
import net.arville.util.CriteriaQueryBuilder;
import net.arville.util.PageableBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookActivityRepository bookActivityRepository;

    private final RestTemplate restTemplate;
    private final EntityManager em;

    public BookService(BookRepository bookRepository, BookActivityRepository bookActivityRepository, RestTemplate restTemplate, EntityManager em) {
        this.bookRepository = bookRepository;
        this.bookActivityRepository = bookActivityRepository;
        this.restTemplate = restTemplate;
        this.em = em;
    }

    public Book addBook(Book book) {
        bookActivityRepository.save(new BookActivity("INSERT", new Book(), book));
        return bookRepository.save(book);
    }

    public PaginationResponse getAllBookBy(
            String bookName,
            String author,
            String startDateStr,
            String endDateStr,
            String sortType,
            String sortField,
            Integer pageNumber
    ) {
        CriteriaQueryBuilder<Book> queryBuilder = new CriteriaQueryBuilder<>(em, Book.class);

        // Attribute filtering
        if (bookName != null) {
            queryBuilder.with("bookName", SpecificationOperation.LIKE, bookName);
        }

        if (author != null) {
            queryBuilder.with("author", SpecificationOperation.LIKE, author);
        }

        if (startDateStr != null) {
            LocalDateTime startDate = LocalDate.parse(startDateStr).atStartOfDay();
            queryBuilder.with("createdAt", SpecificationOperation.GREATER_THAN_OR_EQUAL, startDate);
        }

        if (endDateStr != null) {
            LocalDateTime endDate = LocalDate.parse(endDateStr).atStartOfDay();
            queryBuilder.with("createdAt", SpecificationOperation.LESS_THAN_OR_EQUAL, endDate);
        }

        // Pagination option
        PageableBuilder pageableBuilder = new PageableBuilder("id");
        if (sortType != null) {
            switch (sortType) {
                case "asc":
                    pageableBuilder.setSortType(Sort.Direction.ASC);
                    break;
                case "desc":
                    pageableBuilder.setSortType(Sort.Direction.DESC);
                    break;
            }
        }

        if (sortField != null) {
            pageableBuilder.addSortField(Arrays.asList(sortField.split(",")));
        }

        if (pageNumber != null) {
            pageableBuilder.setPageNumber(pageNumber);
        }

        queryBuilder.setPageable(pageableBuilder.build());

        queryBuilder.selects("id", "bookName", "author", "price");

        var books = queryBuilder.execute();

        if (books.getSize() == 0) {
            throw new ItemNotFoundException();
        }

        return new PaginationResponse(
                books.get().collect(Collectors.toList()),
                books.getNumber() + 1,
                books.getTotalPages(),
                books.getTotalElements()
        );
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
