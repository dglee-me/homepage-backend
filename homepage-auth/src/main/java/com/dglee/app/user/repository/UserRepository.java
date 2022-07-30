package com.dglee.app.user.repository;

import com.dglee.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Object> {

    Optional<User> findUserByEmail(String email);
}
