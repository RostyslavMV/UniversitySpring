package com.rmv.oop.university.repo;

import com.rmv.oop.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
