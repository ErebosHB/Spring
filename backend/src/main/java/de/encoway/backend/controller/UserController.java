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


@RestController
public class UserController {

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
                if (username.length() <= 64) {
                    user.setUsername(username);
                    if (password.length() <= 10) {
                        user.setPassword(password);
                        userRepository.save(user);
                    } else {
                        return HttpStatus.NOT_ACCEPTABLE;
                    }
                } else {
                    return HttpStatus.NOT_ACCEPTABLE;
                }
                return HttpStatus.CREATED;
            } else {
                return HttpStatus.NOT_ACCEPTABLE;
            }
        } else {
            return HttpStatus.NOT_ACCEPTABLE;
        }

    }

    @DeleteMapping("/user/delete")
    public HttpStatus deleteUser(@RequestParam String username) {
        if (userRepository.existsById(userRepository.findUserByUsername(username).getId())) {
            userRepository.delete(userRepository.findUserByUsername(username));
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_IMPLEMENTED;
        }


    }

    @GetMapping("/user/data")
    public ResponseEntity<User> getUserData(@RequestParam String username) {
        if (!userRepository.existsById(userRepository.findUserByUsername(username).getId())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(userRepository.findUserByUsername(username), HttpStatus.OK);
        }

    }


    @PostMapping("/user/update")
    public HttpStatus updateUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findUserByUsername(username).getPassword() == password) {
            return HttpStatus.CONFLICT;
        } else {
            userRepository.findUserByUsername(username).setPassword(password);
            userRepository.save(userRepository.findUserByUsername(username));
            return HttpStatus.OK;
        }

    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> findAllUser() {

        List<User> allUser = new ArrayList<User>();
        userRepository.findAll().forEach(allUser::add);
        if (allUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }

    }


}
