package de.encoway.backend.controller;

import de.encoway.backend.model.User;
import de.encoway.backend.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;



@RestController
public class UserController {
    private int maxLengthUsername = 64;
    private int maxLengthPassword = 10;

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/create")
    public HttpStatus createUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        if (!Objects.equals(username, "")) {
            if (!Objects.equals(password, "")) {
                if (username.length() <= maxLengthUsername) {
                    user.setUsername(username);
                    if (password.length() <= maxLengthPassword) {
                        user.setPassword(password);
                        userRepository.save(user);
                        return CREATED;
                    } else return NOT_ACCEPTABLE;
                } else return NOT_ACCEPTABLE;
            } else return NOT_ACCEPTABLE;
        } else return NOT_ACCEPTABLE;

    }

    @DeleteMapping("/user/delete")
    public HttpStatus deleteUser(@RequestParam String username) {
        if (userRepository.findUserByUsername(username) != null) {
            userRepository.delete(userRepository.findUserByUsername(username));
            return OK;
        } else return NO_CONTENT;


    }

    @GetMapping("/user/data")
    public ResponseEntity<User> getUserData(@RequestParam String username) {
        if (userRepository.findUserByUsername(username) != null) {
            return new ResponseEntity<>(userRepository.findUserByUsername(username), OK);
        } else return new ResponseEntity<>(EXPECTATION_FAILED);

    }


    @PostMapping("/user/update")
    public HttpStatus updateUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findUserByUsername(username) != null) {
            if (password.equals(userRepository.findUserByUsername(username).getPassword())) {
                return FORBIDDEN;
            }
            userRepository.findUserByUsername(username).setPassword(password);
            userRepository.save(userRepository.findUserByUsername(username));
            return OK;
        } else return CONFLICT;

    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> findAllUser() {
        List<User> allUser = new ArrayList<User>();
        userRepository.findAll().forEach(allUser::add);
        if (allUser.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        } else return new ResponseEntity<>(allUser, OK);

    }


}
