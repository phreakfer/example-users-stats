package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionRepository  extends CrudRepository<Transaction, Long> {
    /*
    SELECT
        DATE(date) AS fromdate,
        DATE(date) AS todate,
        SUM(earning) as earning
    FROM transaction
    WHERE user_id= 1 AND DATE(date) >= '2021-07-05' AND DATE(date) <= '2021-09-05'
    GROUP BY fromdate, todate
    ORDER BY fromdate ASC;
     */
    @Query(value = "SELECT\n" +
            "        DATE(date) AS fromdate,\n" +
            "        DATE(date) AS todate,\n" +
            "        SUM(earning) as earning\n" +
            "    FROM transaction\n" +
            "    WHERE user_id= :id AND DATE(date) >= :startDate AND DATE(date) <= :endDate\n" +
            "    GROUP BY fromdate, todate\n" +
            "    ORDER BY fromdate ASC;", nativeQuery = true)
    List<Map<String, Object>> findStatsByUserIdDaily(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /*
    SELECT
        STR_TO_DATE(CONCAT(DATE_FORMAT(date, "%Y"),'-',DATE_FORMAT(date, "%U"),'Sunday'), '%X-%V %W') AS `fromdate`,
        DATE_ADD(STR_TO_DATE(CONCAT(DATE_FORMAT(date, "%Y"),'-',DATE_FORMAT(date, "%U"),'Sunday'), '%X-%V %W'), INTERVAL 7 DAY) AS `todate`,
        SUM(earning) as earning
    FROM transaction
    WHERE user_id= '1' AND DATE(date) >= '2021-07-05' AND DATE(date) <= '2021-09-05'
    GROUP BY fromdate,todate
    ORDER BY fromdate ASC;
     */
    @Query(value = "SELECT\n" +
            "        STR_TO_DATE(CONCAT(DATE_FORMAT(date, \"%Y\"),'-',DATE_FORMAT(date, \"%U\"),'Sunday'), '%X-%V %W') AS `fromdate`,\n" +
            "        DATE_ADD(STR_TO_DATE(CONCAT(DATE_FORMAT(date, \"%Y\"),'-',DATE_FORMAT(date, \"%U\"),'Sunday'), '%X-%V %W'), INTERVAL 6 DAY) AS `todate`,\n" +
            "        SUM(earning) as earning\n" +
            "    FROM transaction " +
            "    WHERE user_id= :id AND DATE(date) >= :startDate AND DATE(date) <= :endDate\n" +
            "    GROUP BY fromdate,todate\n" +
            "    ORDER BY fromdate ASC;", nativeQuery = true)
    List<Map<String, Object>> findStatsByUserIdWeekly(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /*
    SELECT
        (LAST_DAY(date - INTERVAL 1 MONTH) + INTERVAL 1 DAY) as fromdate,
        LAST_DAY(date) as todate,
        SUM(earning) as earning
    FROM transaction
    WHERE user_id= '1' AND DATE(date) >= '2021-07-05' AND DATE(date) <= '2021-09-05'
    GROUP BY fromdate, todate
    ORDER BY fromdate ASC;
     */
    @Query(value = "SELECT\n" +
            "        (LAST_DAY(date - INTERVAL 1 MONTH) + INTERVAL 1 DAY) as fromdate,\n" +
            "        LAST_DAY(date) as todate,\n" +
            "        SUM(earning) as earning\n" +
            "    FROM transaction\n" +
            "    WHERE user_id= :id AND DATE(date) >= :startDate AND DATE(date) <= :endDate\n" +
            "    GROUP BY fromdate, todate\n" +
            "    ORDER BY fromdate ASC;", nativeQuery = true)
    List<Map<String, Object>> findStatsByUserIdMonthly(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    // Example JPQL
    //@Query(value = "SELECT t FROM Transaction t WHERE t.user = :user")
    //List<Transaction> findStatsByUserId(@Param("user") User user);
}

