package net.arville.controller;

import net.arville.enumeration.ErrorCode;
import net.arville.payload.*;
import net.arville.service.BookActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book-activity")
public class BookActivityController {

    private final BookActivityService bookActivityService;

    public BookActivityController(BookActivityService bookActivityService) {
        this.bookActivityService = bookActivityService;
    }

    @GetMapping()
    public ResponseEntity<ResponseBodyHandler> getAllBookActivity() {
        ResponseBodyHandler responseBody;

        var bookActivityList = bookActivityService.getAllBookActivity();
        responseBody = ErrorCode.SUCCESS.Response(bookActivityList);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
