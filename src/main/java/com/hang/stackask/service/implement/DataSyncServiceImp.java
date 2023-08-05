package com.hang.stackask.service.implement;

import com.hang.stackask.converter.EntityToDocumentConverter;
import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import com.hang.stackask.esrepository.QuestionEsRepository;
import com.hang.stackask.service.interfaces.IDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DataSyncServiceImp implements IDataSyncService {
    private QuestionEsRepository questionEsRepository;
    private EntityToDocumentConverter entityToDocumentConverter;

    @Autowired
    public DataSyncServiceImp(
            QuestionEsRepository questionEsRepository,
            EntityToDocumentConverter entityToDocumentConverter){

        this.questionEsRepository = questionEsRepository;
        this.entityToDocumentConverter = entityToDocumentConverter;
    }

    @Async
    public void syncDataToElasticsearch(Question question) {
        QuestionDoc questionDocument = entityToDocumentConverter.toDocument(question);
        questionEsRepository.save(questionDocument);
    }
}
