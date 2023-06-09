//package com.muzio.model;
//
//import com.muzio.enums.TransactionType;
//import jakarta.persistence.*;
//
//import java.util.Date;
//import java.util.Objects;
//
//@Entity
//@Table(name = "transaction")
//public class Transaction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
//
//    private CreditCard creditCard;
//    private String type;
//
//    private Integer amount;
//    private Date time;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public CreditCard getCreditCard() {
//        return creditCard;
//    }
//
//    public void setCreditCard(CreditCard creditCard) {
//        this.creditCard = creditCard;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Integer getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Integer amount) {
//        this.amount = amount;
//    }
//
//    public Date getTime() {
//        return time;
//    }
//
//    public void setTime(Date time) {
//        this.time = time;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Transaction that = (Transaction) o;
//        return Objects.equals(id, that.id) && Objects.equals(creditCard, that.creditCard) && type == that.type && Objects.equals(amount, that.amount) && Objects.equals(time, that.time);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, creditCard, type, amount, time);
//    }
//
//    @Override
//    public String toString() {
//        return "Transaction{" +
//                "id=" + id +
//                ", creditCard=" + creditCard +
//                ", type=" + type +
//                ", amount=" + amount +
//                ", time=" + time +
//                '}';
//    }
//}
