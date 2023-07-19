package com.hang.stackask.repository;

import com.hang.stackask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmailAndEnabledIsTrue(String email);
    User getUserByResetPasswordTokenAndEnabledIsTrue(String resetPassword);
}