package com.wsda.repository;

import com.wsda.model.WSDARole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WSDARoleRepository extends JpaRepository<WSDARole, Integer>{
    WSDARole findWSDARoleByRole(String role);
}