package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    //Get users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    //Get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUsers(@PathVariable Long id) {
        Optional<User> possibbleUser = userRepository.findById(id);
        if (possibbleUser.isPresent()){
            return new ResponseEntity<>(possibbleUser.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }

    //Save user
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> postUsers(@RequestBody UserDTO userDTO) {
        User newUser = new User(userDTO.getName(), userDTO.getAddress());
        return new ResponseEntity<>(userRepository.save(newUser),HttpStatus.OK);
    }
}