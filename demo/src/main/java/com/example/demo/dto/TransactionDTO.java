package com.example.demo.dto;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDTO {
    private String field1;
    private String field2;
    private BigDecimal earning;
    private Long userId;

    public String getField1() { return field1;}
    public String getField2() { return field2;}
    public BigDecimal getEarning() { return earning;}
    public Long getUserId() { return userId;}

    public void setField1(String field1) { this.field1 = field1;}
    public void setField2(String field2) { this.field2 = field2;}
    public void setEarning(BigDecimal earning) { this.earning = earning;}
    public void setUserId(Long userId) { this.userId = userId;}

    public TransactionDTO(){}
    public TransactionDTO(String field1, String field2, BigDecimal earning, Long userId) {
        this.field1 = field1;
        this.field2 = field2;
        this.earning = earning;
        this.userId = userId;
    }
}
