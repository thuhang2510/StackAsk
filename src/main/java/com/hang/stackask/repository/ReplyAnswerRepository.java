package com.hang.stackask.repository;

import com.hang.stackask.entity.ReplyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyAnswerRepository extends JpaRepository<ReplyAnswer, Long> {
    ReplyAnswer getByIdAndEnabledIsTrue(Long id);
}
