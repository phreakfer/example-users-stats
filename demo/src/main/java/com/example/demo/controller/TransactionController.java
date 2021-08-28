package com.example.demo.controller;

import com.example.demo.dto.StatDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.enums.TimeEnum;
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
            @RequestParam(value = "startdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(value = "timeenum", required = false, defaultValue = "DAILY") TimeEnum timeEnum) throws ParseException {

        String key, stringFromDate, stringToDate;
        Date fromDate, toDate;
        BigDecimal earning;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        TreeMap<String, StatDTO> listBD = new TreeMap();
        List<StatDTO> listStatDTO = new ArrayList<StatDTO>();
        List<Map<String, Object>> rows;

        if (timeEnum == TimeEnum.DAILY) {
            // DAILY
            rows = transactionRepository.findStatsByUserIdDaily(id, startDate, endDate);
            for (Map<String, Object> row : rows){
                fromDate = formatDate.parse(row.get("fromdate").toString());
                toDate = formatDate.parse(row.get("todate").toString());
                earning = new BigDecimal(row.get("earning").toString());
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                listBD.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill DAILY with 0
            Date day = startDate;
            do{
                fromDate = day;
                toDate = fromDate;
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                boolean isKeyPresent = listBD.containsKey(key);
                if (isKeyPresent) {
                    earning = listBD.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listStatDTO.add(new StatDTO(stringFromDate, stringToDate, earning));
                day = plusDaysToDate(day, 1);
            }while(day.before(endDate) || day.equals(endDate));
            return new ResponseEntity<>(listStatDTO, HttpStatus.OK);
        }

        else if (timeEnum == TimeEnum.WEEKLY) {
            // WEEKLY
            rows = transactionRepository.findStatsByUserIdWeekly(id, startDate, endDate);
            for (Map<String, Object> row : rows){
                fromDate = formatDate.parse(row.get("fromdate").toString());
                toDate = formatDate.parse(row.get("todate").toString());
                earning = new BigDecimal(row.get("earning").toString());
                if (fromDate.before(startDate)) {
                    fromDate = startDate;
                }
                if (toDate.after(endDate)) {
                    toDate = endDate;
                }
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                listBD.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill WEEKLY with 0
            Date firstDayOfWeek = getFirstDayOfWeek(startDate);
            do {
                Date lastDayOfWeek = plusDaysToDate(firstDayOfWeek, 6);
                if (firstDayOfWeek.before(startDate)) {
                    fromDate = startDate;
                } else {
                    fromDate = firstDayOfWeek;
                }
                if (lastDayOfWeek.after(endDate)) {
                    toDate = endDate;
                } else {
                    toDate = lastDayOfWeek;
                }
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                boolean isKeyPresent = listBD.containsKey(key);
                if (isKeyPresent) {
                    earning = listBD.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listStatDTO.add(new StatDTO(stringFromDate, stringToDate, earning));
                firstDayOfWeek = plusDaysToDate(firstDayOfWeek, 7);
            } while (firstDayOfWeek.before(endDate) || firstDayOfWeek.equals(endDate));
            return new ResponseEntity<>(listStatDTO, HttpStatus.OK);
        }

        else {
            // MONTHLY
            rows = transactionRepository.findStatsByUserIdMonthly(id, startDate, endDate);
            for (Map<String, Object> row : rows){
                fromDate = formatDate.parse(row.get("fromdate").toString());
                toDate = formatDate.parse(row.get("todate").toString());
                earning = new BigDecimal(row.get("earning").toString());
                if (fromDate.before(startDate)) {
                    fromDate = startDate;
                }
                if (toDate.after(endDate)) {
                    toDate = endDate;
                }
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                listBD.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill MONTHLY with 0
            Date firstDayOfMonth = getFirstDayOfMonth(startDate);
            do {
                Date lastDayOfMonth = plusDaysToDate(plusMonthsToDate(firstDayOfMonth, 1),-1);
                if (firstDayOfMonth.before(startDate)) {
                    fromDate = startDate;
                } else {
                    fromDate = firstDayOfMonth;
                }
                if (lastDayOfMonth.after(endDate)) {
                    toDate = endDate;
                } else {
                    toDate = lastDayOfMonth;
                }
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                boolean isKeyPresent = listBD.containsKey(key);
                if (isKeyPresent) {
                    earning = listBD.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listStatDTO.add(new StatDTO(stringFromDate, stringToDate, earning));
                firstDayOfMonth = plusMonthsToDate(firstDayOfMonth, 1);
            } while (firstDayOfMonth.before(endDate) || firstDayOfMonth.equals(endDate));
            return new ResponseEntity<>(listStatDTO, HttpStatus.OK);
        }
    }

    public static Date plusDaysToDate(Date date, int days) {
        if (days == 0) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date plusMonthsToDate(Date date, int months){
        if (months==0) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date getFirstDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    public static Date getFirstDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }


}
