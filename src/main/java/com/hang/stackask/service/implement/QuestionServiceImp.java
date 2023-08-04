package com.hang.stackask.service.implement;

import com.hang.stackask.converter.StringToTypePrimitiveConverter;
import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.esrepository.QuestionEsRepository;
import com.hang.stackask.exception.QuestionNotFoundException;
import com.hang.stackask.data.QuestionData;
import com.hang.stackask.entity.Question;
import com.hang.stackask.exception.QuestionsDataNotFoundException;
import com.hang.stackask.repository.QuestionRepository;
import com.hang.stackask.request.QuestionRequest;
import com.hang.stackask.service.interfaces.IDataSyncService;
import com.hang.stackask.service.interfaces.IQuestionService;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.hang.stackask.utils.MapperUtil.mapList;

@Service
public class QuestionServiceImp implements IQuestionService {
    @Autowired
    private QuestionRepository questionJpaRepository;

    @Autowired
    private QuestionEsRepository questionEsRepository;

    @Autowired
    private IDataSyncService iDataSyncService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionData getByUuid(String uuid){
        Question question = questionJpaRepository.getByUuidAndEnabledIsTrue(uuid);

        if(question == null)
            throw new QuestionNotFoundException("question not found");

        QuestionData questionData = modelMapper.map(question, QuestionData.class);
        return questionData;
    }

    @Override
    @Transactional
    public QuestionData create(Long userId, QuestionRequest questionRequest) {
        iUserService.getById(userId);

        Question question = modelMapper.map(questionRequest, Question.class);

        String uuid = UUID.randomUUID().toString();
        question.setUuid("Q" + uuid);
        question.setUserId(userId);

        Question createdQuestion = questionJpaRepository.save(question);
        iDataSyncService.syncDataToElasticsearch();

        QuestionData questionData = modelMapper.map(createdQuestion, QuestionData.class);
        return questionData;
    }

    private List<QuestionData> getQuestions(int limit) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Question> questions = questionJpaRepository.findQuestionOrOrderByCreatedTimeAndId(pageable);

        if(questions == null)
            throw new QuestionsDataNotFoundException("Unable to get list of questions");

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }

    @Override
    public List<QuestionData> getQuestionsWithPagination(String nextPageToken, int limit) throws UnsupportedEncodingException {
        if(StringUtil.isNoContent(nextPageToken))
            return getQuestions(limit);

        String decodedNextPageToken = StringUtil.newUtf(Base64.getDecoder().decode(nextPageToken));

        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Question> questions = questionJpaRepository.findQuestions(
                StringToTypePrimitiveConverter.toInt(decodedNextPageToken), pageable);

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }

    private List<QuestionData> getByTitleOrContent(String keyword, int limit) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        List<QuestionDoc> questionsDoc = questionEsRepository.findByTitleOrContentAndEnabledIsTrueOrderByIdDesc(
                keyword, keyword, pageable);

        if(questionsDoc == null)
            throw new QuestionsDataNotFoundException("Unable to get list of questions");

        List<QuestionData> questionsData = mapList(questionsDoc, QuestionData.class);
        return questionsData;
    }


    @Override
    public List<QuestionData> getByTitleOrContentWithPagination(
            String keyword, String nextPageToken, int limit) throws UnsupportedEncodingException {

        if(StringUtil.isNoContent(nextPageToken))
            return getByTitleOrContent(keyword, limit);

        String decodedNextPageToken = StringUtil.newUtf(Base64.getDecoder().decode(nextPageToken));
        Pageable pageable = PageRequest.of(0, limit + 1, Sort.by("id").descending());

        List<QuestionDoc> questionsDoc = questionEsRepository.findByTitleOrContentAndEnabledIsTrue(
                keyword, keyword, StringToTypePrimitiveConverter.toInt(decodedNextPageToken), pageable);

        if(questionsDoc == null)
            throw new QuestionsDataNotFoundException("Unable to get list of questions");

        List<QuestionData> questionsData = mapList(questionsDoc, QuestionData.class);
        return questionsData;
    }
}
