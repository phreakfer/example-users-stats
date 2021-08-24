/*
package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class StatController {


    //@Autowired
    //UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //Get stats by user
    @GetMapping("/users/{id}/stats")
    public ResponseEntity<?> getStatsByUser(@PathVariable Long id, @RequestParam(value = "startdate") Date startDate, @RequestParam(value = "enddate") Date endDate) {
        return new ResponseEntity<>(transactionRepository.findStatsById(id,startDate,endDate), HttpStatus.OK);
    }
}

 */
