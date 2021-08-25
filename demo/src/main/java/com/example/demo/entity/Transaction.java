package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String field1;
    private String field2;
    private BigDecimal earning;
    //@CreationTimestamp
    private Date date;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public long getId() {
        return id;
    }
    public String getField1() {
        return field1;
    }
    public String getField2() {
        return field2;
    }
    public BigDecimal getEarning() {
        return earning;
    }
    public Date getDate() {
        return date;
    }
    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setField1(String field1) {
        this.field1 = field1;
    }
    public void setField2(String field2) {
        this.field2 = field2;
    }
    public void setEarning(BigDecimal earning) {
        this.earning = earning;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Transaction(){}
    public Transaction(String field1, String field2, BigDecimal earning, User user) {
        this.field1 = field1;
        this.field2 = field2;
        this.earning = earning;
        this.user = user;
        this.date = new Date();
    }
}
