package com.muzio.repository;

import com.muzio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u WHERE u.email = ?1")
    User findByEmail(String email);
}

