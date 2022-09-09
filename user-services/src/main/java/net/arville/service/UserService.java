package net.arville.service;

import net.arville.exception.EmailAlreadyUsedException;
import net.arville.exception.UserNotFoundException;
import net.arville.model.User;
import net.arville.repository.UserRepository;
import net.arville.repository.UsersBookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UsersBookRepository usersBookRepository;

    public UserService(UserRepository userRepository, UsersBookRepository usersBookRepository) {
        this.userRepository = userRepository;
        this.usersBookRepository = usersBookRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {

        Optional<User> checkUser = userRepository.findUserByEmail(user.getEmail());

        if (checkUser.isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        return userRepository.save(user);
    }

    public boolean checkIfUserExist(Long id) {
        Optional<User> res = userRepository.findById(id);
        return res.isPresent();
    }

    public User getUserByUserId(Long id) {
        var res = userRepository.findById(id);
        if (res.isPresent()) {
            return res.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public User updateUser(Long userId, User user) {
        String newEmail = user.getEmail();
        String newName = user.getName();
        String newPhone = user.getPhone();
        LocalDate newDob = user.getDob();

        User toUpdate = getUserByUserId(userId);

        if (newEmail != null && newEmail.length() > 0) {
            toUpdate.setEmail(newEmail);
        }

        if (newName != null && newName.length() > 0) {
            toUpdate.setName(newName);
        }

        if (newPhone != null && newPhone.length() > 0) {
            toUpdate.setPhone(newPhone);
        }

        if (newDob != null) {
            toUpdate.setDob(newDob);
        }

        return toUpdate;
    }

    public User deleteUser(Long userId) {
        User deletedUser = getUserByUserId(userId);

        Long deletedUserId = deletedUser.getId();
        var deletedUsersBook = usersBookRepository.findAllByUserId(deletedUserId);
        usersBookRepository.deleteAll(deletedUsersBook);

        userRepository.delete(deletedUser);
        return deletedUser;
    }
}
