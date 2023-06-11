package com.muzio.repository;

import com.muzio.model.Role;
import com.muzio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u WHERE u.email = ?1")
    User findByEmail(String email);

    List<User> findByRole(Role role);
}

