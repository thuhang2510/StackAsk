package com.hang.stackask.service.implement;

import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import com.hang.stackask.esrepository.QuestionEsRepository;
import com.hang.stackask.mapper.interfaces.IQuestionMapper;
import com.hang.stackask.service.interfaces.IDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DataSyncServiceImp implements IDataSyncService {
    private QuestionEsRepository questionEsRepository;
    private IQuestionMapper iQuestionMapper;

    @Autowired
    public DataSyncServiceImp(
            QuestionEsRepository questionEsRepository,
            IQuestionMapper iQuestionMapper){

        this.questionEsRepository = questionEsRepository;
        this.iQuestionMapper = iQuestionMapper;
    }

    @Async
    public void syncDataToElasticsearch(Question question) {
        QuestionDoc questionDocument = iQuestionMapper.toDocument(question);
        questionEsRepository.save(questionDocument);
    }
}
