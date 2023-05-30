package com.wsda.repository;

import com.wsda.model.WSDAToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WSDATokenRepository extends JpaRepository<WSDAToken, Integer>{
    List<WSDAToken> findWSDATokenByTokenAndLoggedOutIsNull(String token);
}

