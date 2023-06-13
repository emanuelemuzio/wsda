package com.muzio.repository;

import com.muzio.model.Role;
import com.muzio.model.Store;
import com.muzio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u WHERE u.email = ?1")
    User findByEmail(String email);

    List<User> findByRole(Role role);
    @Query("Select u FROM User u INNER JOIN u.registeredStores urs INNER JOIN u.role ur WHERE ur.name = ?1 AND urs.name = ?2")
    List<User> findStoreCustomers(String roleName, String storeName);
}

