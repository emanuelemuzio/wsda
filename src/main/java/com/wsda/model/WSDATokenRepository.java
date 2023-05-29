package com.wsda.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WSDATokenRepository extends JpaRepository<WSDAToken, Integer>{
    List<WSDAToken> findWSDATokenByTokenAndLoggedOutIsNull(String token);
}

