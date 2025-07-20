package com.apis.postgrestesting.repository;

import com.apis.postgrestesting.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findByEmail(String email) ;
}
