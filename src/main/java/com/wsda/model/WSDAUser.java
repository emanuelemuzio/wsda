package com.wsda.model;

import jakarta.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "wsda_user")
public class WSDAUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Integer id;
    private String name;
    private String email;
    private String password;
    private Integer age;

    public WSDAUser(Integer id, String name, String email, String password,Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WSDAUser(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WSDAUser wsdaUser = (WSDAUser) o;
        return Objects.equals(id, wsdaUser.id) && Objects.equals(name, wsdaUser.name) && Objects.equals(email, wsdaUser.email) && Objects.equals(password, wsdaUser.password) && Objects.equals(age, wsdaUser.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, age);
    }

    @Override
    public String toString() {
        return "WSDAUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
