package com.muzio.repository;

import com.muzio.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TokenRepository extends JpaRepository<Token, Integer>{
    List<Token> findTokenByTokenAndLoggedOutIsNull(String token);
}

