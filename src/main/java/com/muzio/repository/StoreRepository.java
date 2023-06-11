package com.muzio.repository;

import com.muzio.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StoreRepository extends JpaRepository<Store, Integer>{
    List<Store> findByOrderByNameAsc();
}