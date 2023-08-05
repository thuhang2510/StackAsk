package com.hang.stackask.esrepository;

import com.hang.stackask.document.QuestionDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionEsRepository extends CrudRepository<QuestionDoc, Long> {
    @Query("{ \"bool\": { \"must\": [ { \"bool\": { \"should\": [ { \"match\": { \"title\": \"?0\" } }, { \"match\": { \"content\": \"?0\" } } ] } }, { \"term\": { \"enabled\": true } } ] } } ")
    List<QuestionDoc> findByKeyword(String content, Pageable pageable);

    @Query("{ \"bool\": { \"must\": [ { \"bool\": { \"should\": [ { \"match\": { \"title\": \"?0\" } }, { \"match\": { \"content\": \"?0\" } } ] } }, { \"term\": { \"enabled\": true } }, { \"range\": { \"id\": { \"lt\": ?1 } } } ] } } ")
    List<QuestionDoc> findByKeywordWithPagination(String keyword, int id, Pageable pageable);
}
