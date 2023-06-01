package com.wsda.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wsda_role")
public class WSDARole {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Integer id;
    private String role;

    public Integer getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WSDARole wsdaRole = (WSDARole) o;
        return Objects.equals(id, wsdaRole.id) && Objects.equals(role, wsdaRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WSDARole{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
