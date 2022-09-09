package net.arville.service;

import net.arville.exception.BookNotFoundException;
import net.arville.exception.UserAlreadyHasThisBookException;
import net.arville.exception.UserNotFoundException;
import net.arville.model.Book;
import net.arville.model.UsersBook;
import net.arville.repository.UsersBookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UsersBookService {
    private final UsersBookRepository usersBookRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;

    public UsersBookService(UsersBookRepository usersBookRepository, UserService userService, RestTemplate restTemplate) {
        this.usersBookRepository = usersBookRepository;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    public List<UsersBook> getAllUsersBook(Long userId) {
        return usersBookRepository.findAllByUserId(userId);
    }

    public boolean checkIfBookExist(Long bookId) {
        String endpoint = "http://book-services/api/book/" + bookId;

        try {
            restTemplate.getForEntity(endpoint, String.class);
            return true;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
                throw e;
            }
            return false;
        }

    }

    public UsersBook addNewUsersBook(UsersBook usersBook) {
        if (!userService.checkIfUserExist(usersBook.getUserId())) {
            throw new UserNotFoundException();
        }

        if (!this.checkIfBookExist(usersBook.getBookId())) {
            throw new BookNotFoundException();
        }

        if (this.checkIfUserAlreadyHasTheBook(usersBook)) {
            throw new UserAlreadyHasThisBookException();
        }

        return usersBookRepository.save(usersBook);
    }

    private boolean checkIfUserAlreadyHasTheBook(UsersBook usersBook) {
        Optional<UsersBook> res = usersBookRepository.findByUserIdAndBookId(usersBook.getUserId(), usersBook.getBookId());
        return res.isPresent();
    }

    public List<UsersBook> deleteAllUsersBookThatHas(Book book) {
        var deletedUsersBook = usersBookRepository.findAllByBookId(book.getId());
        usersBookRepository.deleteAll(deletedUsersBook);
        return deletedUsersBook;
    }
}
