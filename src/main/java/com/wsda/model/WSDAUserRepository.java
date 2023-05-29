package com.wsda.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WSDAUserRepository extends JpaRepository<WSDAUser, Integer>{
    List<WSDAUser> findOneByEmailAndPassword(String email, String password);
}

