package com.wsda.model;

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
    @Column(unique = true)
    private String number;
    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "wsda_user_id", referencedColumnName = "id")

    private WSDAUser wsda_user;

    public WSDAUser getWsda_user() {
        return wsda_user;
    }

    public void setWsda_user(WSDAUser wsda_user) {
        this.wsda_user = wsda_user;
    }

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
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(balance, that.balance) && Objects.equals(wsda_user, that.wsda_user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, balance, wsda_user);
    }

    @Override
    public String toString() {
        return "WSDACreditCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", wsda_user=" + wsda_user +
                '}';
    }
}
