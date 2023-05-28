package com.wsda.entity;

import jakarta.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "wsda_credit_card")
public class WSDACreditCard {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Integer id;
    private String number;
    private Integer balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WSDACreditCard that = (WSDACreditCard) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, balance);
    }

    @Override
    public String toString() {
        return "WSDACreditCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                '}';
    }
}
