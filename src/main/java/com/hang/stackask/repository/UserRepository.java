package com.hang.stackask.repository;

import com.hang.stackask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmailAndPasswordAndEnabledIsTrue(String email, String password);
}