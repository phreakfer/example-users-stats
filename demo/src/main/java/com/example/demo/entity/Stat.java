package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Stat {
    private Date date;
    private BigDecimal earning;

    public Date getDate() {return date;}
    public BigDecimal getEarning() {return earning;}

    public void setDate(Date date) {this.date = date;}
    public void setEarning(BigDecimal earning) {this.earning = earning;}

    public Stat(){}
    public Stat(Date date, BigDecimal earning) {
        this.date = date;
        this.earning = earning;
    }
}
