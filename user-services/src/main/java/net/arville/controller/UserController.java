package net.arville.controller;

import net.arville.enumeration.ErrorCode;
import net.arville.exception.EmailAlreadyUsedException;
import net.arville.exception.UserNotFoundException;
import net.arville.model.User;
import net.arville.payload.ResponseBodyHandler;
import net.arville.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseBodyHandler> getUsers() {
        ResponseBodyHandler body;

        var users = userService.getAllUsers();
        body = ErrorCode.SUCCESS.Response(users);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseBodyHandler> getUsers(@PathVariable Long userId) {
        ResponseBodyHandler body;

        try {
            var user = userService.getUserByUserId(userId);
            body = ErrorCode.SUCCESS.Response(user);
        } catch (UserNotFoundException e) {
            body = ErrorCode.NO_RESULT_FOUND.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping
    public ResponseEntity<ResponseBodyHandler> addUser(@RequestBody User user) {
        ResponseBodyHandler body;
        try {
            User newUser = userService.addUser(user);
            body = ErrorCode.SUCCESS.Response(newUser);
        } catch (EmailAlreadyUsedException e) {
            body = ErrorCode.EMAIL_ALREADY_USED.Response(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PutMapping("{userId}")
    public ResponseEntity<ResponseBodyHandler> updateUser(@PathVariable Long userId, @RequestBody User user) {
        ResponseBodyHandler body;

        try {
            var updatedUserData = userService.updateUser(userId, user);
            body = ErrorCode.SUCCESS.Response(updatedUserData);
        } catch (UserNotFoundException e) {
            body = ErrorCode.NO_RESULT_FOUND.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<ResponseBodyHandler> deleteUser(@PathVariable Long userId) {
        ResponseBodyHandler body;

        try {
            var deletedUser = userService.deleteUser(userId);
            body = ErrorCode.SUCCESS.Response(deletedUser);
        } catch (UserNotFoundException e) {
            body = ErrorCode.NO_RESULT_FOUND.Response(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}
