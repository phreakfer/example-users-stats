package com.example.demo.controller;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class TransactionController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //Get transactions by user
    @GetMapping("/users/{id}/transactions")
    public ResponseEntity<?> getTransactionsByUser(@PathVariable Long id) {
        Optional<User> possibbleUser = userRepository.findById(id);
        if (possibbleUser.isPresent()){
            return new ResponseEntity<>(possibbleUser.get().getTransaction(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }

    //Save transaction of user
    @RequestMapping(value = "/users/{id}/transactions", method = RequestMethod.POST)
    public ResponseEntity<?> postTransactionByUser(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        Optional<User> possibbleUser = userRepository.findById(id);
        if (possibbleUser.isPresent()){
            User user = possibbleUser.get();
            Transaction newTransaction = new Transaction(transactionDTO.getField1(), transactionDTO.getField2(), transactionDTO.getEarning(),user);
            return new ResponseEntity<>(transactionRepository.save(newTransaction),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }

    }

    //Get stats by user
    @GetMapping("/users/{id}/stats")
    public ResponseEntity<?> getStatsByUser(
            @PathVariable Long id,
            @RequestParam(value = "startdate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value = "enddate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        //Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sStartDate);
        //Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(sEndDate);

        List<Map<String, String>> listadb = transactionRepository.findStatsByUserId(id,startDate,endDate);
        //return new ResponseEntity<>(listadb, HttpStatus.OK);

        int days = GetDaysBetweenTwoDates(startDate, endDate);
        Map<String, String> listafull = new HashMap<String, String>();
        Date date = startDate;
        for (int i=0; i<days; i++){
            //LocalDateTime.from(date.toInstant()).plusDays(1);
            listafull.put(date.toString(),"1");
        }
        return new ResponseEntity<>(listafull, HttpStatus.OK);


        /*
        int days = GetDaysBetweenTwoDates(startDate, endDate);
        Map<String, String> listafull = new HashMap<String, String>();
        Date date = startDate;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        for (int i=0; i<days; i++){
            c.add(Calendar.DATE, i);
            date = c.getTime();
            listafull.put(date.toString(),"1");
        }
        return new ResponseEntity<>(listafull, HttpStatus.OK);
        */

        //return new ResponseEntity<>(transactionRepository.findStatsByUserId(id,startDate,endDate), HttpStatus.OK);
    }

    public int GetDaysBetweenTwoDates(Date startDate, Date endDate){
        return (int)( (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    /*
    public ResponseEntity<?> getStatsByUser(@PathVariable Long id, @RequestParam(value = "startdate", required=false) Date startDate, @RequestParam(value = "enddate", required=false) Date endDate) {
        Optional<User> possibbleUser = userRepository.findById(id);
        if (possibbleUser.isPresent()) {
            User user = possibbleUser.get();
            return new ResponseEntity<>(transactionRepository.findStatsByUserId(user), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }
     */

}
