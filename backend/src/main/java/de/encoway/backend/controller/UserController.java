package de.encoway.backend.controller;

import de.encoway.backend.model.User;
import de.encoway.backend.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;


@RestController
public class UserController {

    private int maxLengthUsername = 64;
    int maxLengthPassword = 10;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/create")
    public HttpStatus createUser(@RequestParam String username, @RequestParam String password) {
        if (validateUsername(username) && validatePassword(password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userRepository.save(user);
            return CREATED;
        }
        return FORBIDDEN;
    }

    @DeleteMapping("/user/delete")
    public HttpStatus deleteUser(@RequestParam String username) {
        if (validateDelete(username)) {
            userRepository.delete(userRepository.findUserByUsername(username));
            return OK;
        }
        return NO_CONTENT;
    }

    @GetMapping("/user/data")
    public ResponseEntity<User> getUserData(@RequestParam String username) {
        if (validateData(username)) {
            return new ResponseEntity<>(userRepository.findUserByUsername(username), OK);
        }
        return new ResponseEntity<>(EXPECTATION_FAILED);
    }


    @PostMapping("/user/update")
    public HttpStatus updateUser(@RequestParam String username, @RequestParam String password) {
        if (validateUpdate(username, password)) {
            userRepository.findUserByUsername(username).setPassword(password);
            userRepository.save(userRepository.findUserByUsername(username));
            return OK;
        }
        return CONFLICT;
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> findAllUser() {
        List<User> allUser = new ArrayList<User>();
        userRepository.findAll().forEach(allUser::add);
        if (validateList(allUser)) {
            return new ResponseEntity<>(allUser, OK);
        }
        return new ResponseEntity<>(NOT_FOUND);
    }

    private boolean validateUsername(String username) {
        if (username.length() <= maxLengthUsername) {
            if (!username.contains(" ") && !username.equals("")) {
                if (userRepository.findUserByUsername(username) == null){
                    return username.matches("^[a-zA-Z0-9]*$");
                }
            }
        }
        return false;
    }


    private boolean validatePassword(String password) {
        if (password.length() <= maxLengthPassword){
            if (!password.contains(" ") && !Objects.equals(password, "")){
                return StandardCharsets.US_ASCII.newEncoder().canEncode(password);
            }
        }
        return false;
    }

    private boolean validateDelete(String username) {
        return userRepository.findUserByUsername(username) != null;
    }

    private boolean validateData(String username) {
        return userRepository.findUserByUsername(username) != null;
    }

    private boolean validateUpdate(String username, String password) {
        return userRepository.findUserByUsername(username) != null &&
                !password.equals(userRepository.findUserByUsername(username).getPassword());
    }

    private boolean validateList(List<User> allUser) {
        return !allUser.isEmpty();
    }
}
