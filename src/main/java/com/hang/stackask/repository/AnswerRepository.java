package com.hang.stackask.repository;

import com.hang.stackask.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a JOIN a.question q WHERE a.id = :answerId and a.enabled = true")
    Answer getByIdAndEnabledIsTrue(Long answerId);

    Answer getByUuidAndEnabledIsTrue(String uuid);
}
