package com.example.demo.repository;

import com.example.demo.entity.Stat;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionRepository  extends CrudRepository<Transaction, Long> {

    // SELECT DATE(date) as date, SUM(earning) as earning FROM transaction WHERE user_id= 1 AND date >= '2021-07-05' AND date <= '2021-09-05' GROUP BY DATE(date) ORDER BY date ASC;
    @Query(value = "SELECT DATE(date) as date, SUM(earning) as earning FROM transaction WHERE user_id= :id AND date >= :startDate AND date <= :endDate GROUP BY DATE(date) ORDER BY date ASC", nativeQuery = true)
    //List<Stat> findStatsByUserId(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<Map<String, String>> findStatsByUserId(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //@Query(value = "SELECT * FROM transaction WHERE user_id= :id AND date >= :startDate AND date <= :endDate", nativeQuery = true)
    //List<Transaction> findStatsByUserId(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //@Query(value = "SELECT t FROM Transaction t WHERE t.user = :user")
    //List<Transaction> findStatsByUserId(@Param("user") User user);
}

