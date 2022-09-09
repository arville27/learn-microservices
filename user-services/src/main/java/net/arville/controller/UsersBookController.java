package net.arville.controller;

import net.arville.enumeration.ErrorCode;
import net.arville.exception.BookNotFoundException;
import net.arville.exception.UserAlreadyHasThisBookException;
import net.arville.exception.UserNotFoundException;
import net.arville.model.Book;
import net.arville.model.UsersBook;
import net.arville.payload.ResponseBodyHandler;
import net.arville.service.UsersBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;


@RestController
@RequestMapping("/api/usersbook")
public class UsersBookController {

    private final UsersBookService usersBookService;

    public UsersBookController(UsersBookService usersBookService) {
        this.usersBookService = usersBookService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseBodyHandler> getAllUsersBook(@PathVariable Long userId) {
        ResponseBodyHandler body;

        var usersBook = usersBookService.getAllUsersBook(userId);
        body = ErrorCode.SUCCESS.Response(usersBook);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("{userId}")
    public ResponseEntity<ResponseBodyHandler> addUsersBook(@PathVariable Long userId, @RequestBody UsersBook usersBook) {
        ResponseBodyHandler body;

        // Make sure the user id that will be used for business logic is from the url path
        usersBook.setUserId(userId);

        try {
            UsersBook newUsersBook = usersBookService.addNewUsersBook(usersBook);
            body = ErrorCode.SUCCESS.Response(newUsersBook);
        } catch (BookNotFoundException e) {
            body = ErrorCode.NO_BOOK_FOUND.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (UserNotFoundException e) {
            body = ErrorCode.NO_USER_FOUND.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (UserAlreadyHasThisBookException e) {
            body = ErrorCode.USER_ALREADY_HAS_THE_BOOK.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (HttpStatusCodeException e) {
            body = ErrorCode.UNKNOWN_ERROR.Response(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping
    public ResponseEntity<ResponseBodyHandler> deleteAllUsersBookThatHas(@RequestBody Book book) {
        ResponseBodyHandler body;

        var deletedUsersBook = usersBookService.deleteAllUsersBookThatHas(book);
        body = ErrorCode.SUCCESS.Response(deletedUsersBook);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
