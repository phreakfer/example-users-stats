package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

public class StatDTO {
    private String from;
    private String to;
    private BigDecimal earning;

    public String getFrom() { return from;}
    public String getTo() { return to;}
    public BigDecimal getEarning() { return earning;}

    public void setFrom(String from) { this.from = from;}
    public void setTo(String to) { this.to = to;}
    public void setEarning(BigDecimal earning) { this.earning = earning;}

    public StatDTO(){}
    public StatDTO(String from, String to, BigDecimal earning) {
        this.from = from;
        this.to = to;
        this.earning = earning;
    }
}


