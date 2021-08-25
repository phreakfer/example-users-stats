package com.example.demo.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    //@CreationTimestamp
    private Date date;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transaction;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public Date getDate() {
        return date;
    }
    public List<Transaction> getTransaction() { return transaction;}

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setTransaction(List<Transaction> transaction) {this.transaction = transaction;}

    public User(){}
    public User(String name, String address) {
        this.name = name;
        this.address = address;
        this.date = new Date();
    }
}
