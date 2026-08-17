package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository
        extends JpaRepository<AdminUser, Integer> {

    AdminUser findByEmail(String email);
}