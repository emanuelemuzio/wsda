package com.wsda.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
@Entity
@Table(name = "wsda_token")
public class WSDAToken {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Integer id;
    private String token;
    private Date loggedIn;
    private Date loggedOut;
    private Date expiration;

    @ManyToOne
    @JoinColumn(name = "wsda_user_id", referencedColumnName = "id")
    private WSDAUser wsda_user;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Date loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Date getLoggedOut() {
        return loggedOut;
    }

    public void setLoggedOut(Date loggedOut) {
        this.loggedOut = loggedOut;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public WSDAUser getWSDAUser() {
        return wsda_user;
    }

    public void setWSDAUser(WSDAUser wsda_user) {
        this.wsda_user = wsda_user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WSDAToken wsdaToken = (WSDAToken) o;
        return Objects.equals(id, wsdaToken.id) && Objects.equals(token, wsdaToken.token) && Objects.equals(loggedIn, wsdaToken.loggedIn) && Objects.equals(loggedOut, wsdaToken.loggedOut) && Objects.equals(expiration, wsdaToken.expiration) && Objects.equals(wsda_user, wsdaToken.wsda_user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, loggedIn, loggedOut, expiration, wsda_user);
    }

    @Override
    public String toString() {
        return "WSDAToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", loggedIn=" + loggedIn +
                ", loggedOut=" + loggedOut +
                ", expiration=" + expiration +
                ", wsda_user=" + wsda_user +
                '}';
    }
}