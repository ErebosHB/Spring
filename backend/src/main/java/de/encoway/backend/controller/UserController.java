package de.encoway.backend.controller;

import de.encoway.backend.model.User;
import de.encoway.backend.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/create")
    public void createUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    @DeleteMapping("/user/delete")
    public HttpStatus deleteUser(@RequestParam String username) {
        userRepository.delete(userRepository.findUserByUsername(username));
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/user/data")
    public User getUserData(@RequestParam String username) {
        return userRepository.findUserByUsername(username);
    }


    @PostMapping("/user/update")
    public HttpStatus updateUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        userRepository.save(user);
        return HttpStatus.ACCEPTED;

    }

}
