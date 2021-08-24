package com.example.demo.dto;

import java.util.Date;

public class StatDTO {
    private Date from;
    private Date to;
    private double earning;

    public Date getFrom() { return from;}
    public Date getTo() { return to;}
    public double getEarning() { return earning;}

    public void setFrom(Date from) { this.from = from;}
    public void setTo(Date to) { this.to = to;}
    public void setEarning(double earning) { this.earning = earning;}

    public StatDTO(){}
    public StatDTO(Date from, Date to, double earning) {
        this.from = from;
        this.to = to;
        this.earning = earning;
    }
}
