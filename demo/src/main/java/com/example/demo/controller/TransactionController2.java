package com.example.demo.controller;

import com.example.demo.dto.StatDTO;
import com.example.demo.enums.TimeEnum;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class TransactionController2 {

    @Autowired
    TransactionRepository transactionRepository;

    //Get stats by user
    @GetMapping("/users/{id}/stats2")
    public ResponseEntity<?> getStatsByUser(
            @PathVariable Long id,
            @RequestParam(value = "startdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(value = "timeenum", required = false, defaultValue = "DAILY") TimeEnum timeEnum) throws ParseException {

        String key, stringFromDate, stringToDate;
        Date fromDate, toDate;
        BigDecimal earning;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> row;
        TreeMap<String, StatDTO> lista = new TreeMap();
        List<StatDTO> listafull = new ArrayList<StatDTO>();
        List<Map<String, Object>> rows;

        if (timeEnum == TimeEnum.DAILY) {
            // DAILY
            rows = transactionRepository.findStatsByUserIdDaily2(id, startDate, endDate);
            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
                fromDate = formatDate.parse(row.get("fromdate").toString());
                toDate = formatDate.parse(row.get("todate").toString());
                earning = new BigDecimal(row.get("earning").toString());
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                lista.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill DAILY with 0
            int days = GetDaysBetweenTwoDates(startDate, endDate);
            Date date = startDate;
            for (int i = 0; i <= days; i++) {
                fromDate = plusDaysToDate(date, i);
                toDate = fromDate;
                stringFromDate = formatDate.format(fromDate);
                stringToDate = formatDate.format(toDate);
                key = stringFromDate + "#" + stringToDate;
                boolean isKeyPresent = lista.containsKey(key);
                if (isKeyPresent) {
                    earning = lista.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listafull.add(new StatDTO(stringFromDate, stringToDate, earning));
            }
            return new ResponseEntity<>(listafull, HttpStatus.OK);
        }

        else if (timeEnum == TimeEnum.WEEKLY) {
            // WEEKLY
            rows = transactionRepository.findStatsByUserIdWeekly2(id, startDate, endDate);
            Date originalStartDateBd = formatDate.parse(rows.get(0).get("fromdate").toString());
            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
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
                lista.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill WEEKLY with 0
            Date firstDayOfWeek = originalStartDateBd;
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
                boolean isKeyPresent = lista.containsKey(key);
                if (isKeyPresent) {
                    earning = lista.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listafull.add(new StatDTO(stringFromDate, stringToDate, earning));
                firstDayOfWeek = plusDaysToDate(firstDayOfWeek, 7);
            } while (firstDayOfWeek.before(endDate) || firstDayOfWeek.equals(endDate));
            return new ResponseEntity<>(listafull, HttpStatus.OK);
        }

        else {
            // MONTHLY
            rows = transactionRepository.findStatsByUserIdMonthly2(id, startDate, endDate);
            Date originalStartDateBd = formatDate.parse(rows.get(0).get("fromdate").toString());
            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
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
                lista.put(key, new StatDTO(stringFromDate, stringToDate, earning));
            }
            // Fill MONTHLY with 0
            Date firstDayOfMonth = originalStartDateBd;
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
                boolean isKeyPresent = lista.containsKey(key);
                if (isKeyPresent) {
                    earning = lista.get(key).getEarning();
                } else {
                    earning = new BigDecimal(0);
                }
                listafull.add(new StatDTO(stringFromDate, stringToDate, earning));
                firstDayOfMonth = plusMonthsToDate(firstDayOfMonth, 1);
            } while (firstDayOfMonth.before(endDate) || firstDayOfMonth.equals(endDate));
            return new ResponseEntity<>(listafull, HttpStatus.OK);
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

    public int GetDaysBetweenTwoDates(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }


}