package com.example.demo.entity;

import java.util.Date;

public class Stat {
    private Date date;
    private double earning;

    public Date getDate() {return date;}
    public double getEarning() {return earning;}

    public void setDate(Date date) {this.date = date;}
    public void setEarning(double earning) {this.earning = earning;}

    public Stat(){}
    public Stat(Date date, double earning) {
        this.date = date;
        this.earning = earning;
    }
}
