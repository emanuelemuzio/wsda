package com.wsda.repository;

import com.wsda.model.WSDARole;
import com.wsda.model.WSDAUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WSDAUserRepository extends JpaRepository<WSDAUser, Integer>{
    List<WSDAUser> findOneByEmailAndPassword(String email, String password);
    List<WSDAUser> findWSDAUsersBywsdaRole(WSDARole role);
}

