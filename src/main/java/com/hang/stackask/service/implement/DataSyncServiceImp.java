package com.hang.stackask.service.implement;

import com.hang.stackask.converter.EntityToDocumentConverter;
import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import com.hang.stackask.esrepository.QuestionEsRepository;
import com.hang.stackask.repository.QuestionRepository;
import com.hang.stackask.service.interfaces.IDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSyncServiceImp implements IDataSyncService {
    private QuestionRepository questionJpaRepository;
    private QuestionEsRepository questionEsRepository;
    private EntityToDocumentConverter entityToDocumentConverter;

    @Autowired
    public DataSyncServiceImp(
            QuestionRepository questionJpaRepository,
            QuestionEsRepository questionEsRepository,
            EntityToDocumentConverter entityToDocumentConverter){

        this.questionEsRepository = questionEsRepository;
        this.questionJpaRepository = questionJpaRepository;
        this.entityToDocumentConverter = entityToDocumentConverter;
    }

    @Async
    public void syncDataToElasticsearch() {
        List<Question> questions = questionJpaRepository.findAll();

        List<QuestionDoc> questionDocuments = entityToDocumentConverter.toDocuments(questions);
        questionEsRepository.saveAll(questionDocuments);
    }
}
