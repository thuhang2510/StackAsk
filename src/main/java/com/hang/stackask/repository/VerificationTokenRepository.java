package com.hang.stackask.repository;

import com.hang.stackask.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken getVerificationTokenByToken(String token);
}
