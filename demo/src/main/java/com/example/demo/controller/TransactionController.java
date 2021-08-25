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
            @RequestParam(value = "startdate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value = "enddate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
            @RequestParam(value = "timeenum", required=false, defaultValue = "DAILY") TimeEnum timeEnum){

        if (timeEnum == TimeEnum.DAILY) {
            // DAILY
            List<List> rows = transactionRepository.findStatsByUserIdDaily(id, startDate, endDate);
            TreeMap<String, StatDTO> lista = new TreeMap();
            for (int i = 0; i < rows.size(); i++) {
                List row = rows.get(i);
                String stringDate = row.get(0).toString();
                BigDecimal earning = new BigDecimal(row.get(1).toString());
                String key = stringDate + "#" + stringDate;
                lista.put(key, new StatDTO(stringDate, stringDate, earning));
            }
            // Relleno DAILY con 0
            BigDecimal earning;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            int days = GetDaysBetweenTwoDates(startDate, endDate);
            List<StatDTO> listafull = new ArrayList<StatDTO>();
            Date date = startDate;
            for (int i = 0; i <= days; i++) {
                Date nextDay = plusDaysToDate(date, i);
                String stringDate = formatDate.format(nextDay);
                String key = stringDate + "#" + stringDate;
                boolean isKeyPresent = lista.containsKey(key);
                if (isKeyPresent) {
                    earning = lista.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listafull.add(new StatDTO(stringDate, stringDate, earning));
            }
            return new ResponseEntity<>(listafull, HttpStatus.OK);
        }
        else{
            // WEEKLY
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            List<List> rows = transactionRepository.findStatsByUserIdWeekly(id, startDate, endDate);
            TreeMap<String, StatDTO> lista = new TreeMap();
            for (int i = 0; i < rows.size(); i++) {
                List row = rows.get(i);
                String stringWeek = row.get(0).toString();
                String stringYear = row.get(1).toString();
                BigDecimal earning = new BigDecimal(row.get(2).toString());
                Date firstDayOfWeek = getRangeFromWeekYear(Integer.parseInt(stringWeek), Integer.parseInt(stringYear), 0);
                Date lastDayOfWeek = getRangeFromWeekYear(Integer.parseInt(stringWeek), Integer.parseInt(stringYear), 1);
                String from, to;
                if (firstDayOfWeek.before(startDate)){from = formatDate.format(startDate);}
                else{ from = formatDate.format(firstDayOfWeek);}
                if (lastDayOfWeek.after(endDate)){ to = formatDate.format(endDate);}
                else{ to = formatDate.format(lastDayOfWeek);}
                String key = from + "#" + to;
                lista.put(key, new StatDTO(from, to, earning));
            }
            // Relleno el WEEKLY con 0
            BigDecimal earning;
            List<StatDTO> listafull = new ArrayList<StatDTO>();
            int weekStartDate = getWeekAndYearFromDate(startDate,0);
            int yearStartDate = getWeekAndYearFromDate(startDate,1);
            int weekEndDate = getWeekAndYearFromDate(endDate,0);
            int yearEndDate = getWeekAndYearFromDate(endDate,1);
            Date firstDayOfWeekStartDate =  getRangeFromWeekYear(weekStartDate, yearStartDate,0);
            Date lastDayOfWeekEndDate =  getRangeFromWeekYear(weekEndDate, yearEndDate,1);
            Date date = firstDayOfWeekStartDate;
            do{
                Date firstDayOfWeek = date;
                Date lastDayOfWeek = plusDaysToDate(date,6);
                String from, to;
                if (firstDayOfWeek.before(startDate)){from = formatDate.format(startDate);}
                else{ from = formatDate.format(firstDayOfWeek);}
                if (lastDayOfWeek.after(endDate)){ to = formatDate.format(endDate);}
                else{ to = formatDate.format(lastDayOfWeek);}
                String key = from + "#" + to;
                boolean isKeyPresent = lista.containsKey(key);
                if (isKeyPresent) {
                    earning = lista.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listafull.add(new StatDTO(from, to, earning));
                date = plusDaysToDate(date, 7);
            }while(date.before(plusDaysToDate(lastDayOfWeekEndDate,1)) || date.equals(plusDaysToDate(lastDayOfWeekEndDate,1)));
            return new ResponseEntity<>(listafull, HttpStatus.OK);
        }

    }

    public int getWeekAndYearFromDate(Date date, int WeekOrYear){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        if (WeekOrYear == 0){
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            return week-1;
        }
        else{
            int year = calendar.get(Calendar.YEAR);
            return year;
        }
    }
    public Date getRangeFromWeekYear(int week, int year, int StartOrEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        if (StartOrEnd == 0) {
            Date sDate = calendar.getTime();
            return sDate;
        }
        else{
            calendar.add(Calendar.DATE, 6);
            Date eDate = calendar.getTime();
            return eDate;
        }
    }

    public int GetDaysBetweenTwoDates(Date startDate, Date endDate){
        return (int)( (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Date plusDaysToDate(Date date, int days){
        if (days==0) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }


}
