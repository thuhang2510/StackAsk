package com.hang.stackask.repository;

import com.hang.stackask.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question getByUuidAndEnabledIsTrue(String uuid);

    @Query("select e from Question e order by e.id desc")
    List<Question> findQuestionOrOrderByCreatedTimeAndId(Pageable pageable);

    @Query(
            value = "select * from tbl_question e where e.id < ?1 order by e.id desc",
            nativeQuery = true
    )
    List<Question> findQuestions(int id, Pageable pageable);
}
