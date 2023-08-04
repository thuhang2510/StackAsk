package com.hang.stackask.esrepository;

import com.hang.stackask.document.QuestionDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionEsRepository extends CrudRepository<QuestionDoc, Long> {
    List<QuestionDoc> findByTitleOrContentAndEnabledIsTrueOrderByIdDesc(String title, String content, Pageable pageable);

    @Query("{ \"bool\": { \"must\": [ { \"bool\": { \"should\": [ { \"match\": { \"title\": \"?0\" } }, { \"match\": { \"content\": \"?1\" } } ] } }, { \"term\": { \"enabled\": true } }, { \"range\": { \"id\": { \"lt\": ?2 } } } ] } } ")
    List<QuestionDoc> findByTitleOrContentAndEnabledIsTrue(String title, String content, int id, Pageable pageable);
}
