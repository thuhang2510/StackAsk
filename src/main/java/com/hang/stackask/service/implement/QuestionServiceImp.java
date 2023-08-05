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
import java.time.LocalDateTime;
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

    private static final String UNABLED_GET_LIST_QUESTION = "Unable to get list of questions";

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
        iDataSyncService.syncDataToElasticsearch(createdQuestion);

        QuestionData questionData = modelMapper.map(createdQuestion, QuestionData.class);
        return questionData;
    }

    private List<QuestionData> getQuestionsByPageable(Pageable pageable) {
        List<Question> questions = questionJpaRepository.findQuestionOrOrderByCreatedTimeAndId(pageable);

        if(questions == null)
            throw new QuestionsDataNotFoundException(UNABLED_GET_LIST_QUESTION);

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }

    @Override
    public List<QuestionData> getQuestionsWithPagination(String nextPageToken, int limit) throws UnsupportedEncodingException {
        Pageable pageable = PageRequest.of(0, limit + 1);

        if(StringUtil.isNoContent(nextPageToken))
            return getQuestionsByPageable(pageable);

        String decodedNextPageToken = StringUtil.newUtf(Base64.getDecoder().decode(nextPageToken));

        List<Question> questions = questionJpaRepository.findQuestions(
                StringToTypePrimitiveConverter.toInt(decodedNextPageToken), pageable);

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }

    private List<QuestionData> getQuestionsByKeywordAndPageable(String keyword, Pageable pageable) {
        List<QuestionDoc> questionsDoc = questionEsRepository.findByKeyword(keyword, pageable);

        if(questionsDoc == null)
            throw new QuestionsDataNotFoundException(UNABLED_GET_LIST_QUESTION);

        List<QuestionData> questionsData = mapList(questionsDoc, QuestionData.class);
        return questionsData;
    }


    @Override
    public List<QuestionData> getByTitleOrContentWithPagination(
            String keyword, String nextPageToken, int limit) throws UnsupportedEncodingException {

        Pageable pageable = PageRequest.of(0, limit + 1, Sort.by("id").descending());

        if(StringUtil.isNoContent(nextPageToken))
            return getQuestionsByKeywordAndPageable(keyword, pageable);

        String decodedNextPageToken = StringUtil.newUtf(Base64.getDecoder().decode(nextPageToken));

        List<QuestionDoc> questionsDoc = questionEsRepository.findByKeywordWithPagination(
                keyword, StringToTypePrimitiveConverter.toInt(decodedNextPageToken), pageable);

        if(questionsDoc == null)
            throw new QuestionsDataNotFoundException(UNABLED_GET_LIST_QUESTION);

        List<QuestionData> questionsData = mapList(questionsDoc, QuestionData.class);
        return questionsData;
    }

    private Question getById(Long id){
        Question questionEnity = questionJpaRepository.getByIdAndEnabledIsTrue(id);

        if(questionEnity == null)
            throw new QuestionNotFoundException("question not found");

        return questionEnity;
    }

    private QuestionData updateQuestion(Question questionExisted, QuestionRequest questionRequest) {
        if(questionRequest != null){
            questionExisted.setTitle(questionRequest.getTitle());
            questionExisted.setContent(questionRequest.getContent());
            questionExisted.setCategory(questionRequest.getCategory());
        }

        questionExisted.setUpdatedTime(LocalDateTime.now());
        questionExisted = questionJpaRepository.save(questionExisted);
        iDataSyncService.syncDataToElasticsearch(questionExisted);

        return modelMapper.map(questionExisted, QuestionData.class);
    }

    @Override
    @Transactional
    public QuestionData update(Long id, QuestionRequest questionRequest) {
        Question questionExisted = getById(id);
        return updateQuestion(questionExisted, questionRequest);
    }

    @Override
    @Transactional
    public QuestionData updateVote(Long id) {
        Question questionExisted = getById(id);
        Long vote = questionExisted.getVote();

        questionExisted.setVote(++vote);
        return updateQuestion(questionExisted, null);
    }

    @Override
    @Transactional
    public QuestionData updateView(Long id) {
        Question questionExisted = getById(id);
        Long view = questionExisted.getView();

        questionExisted.setView(++view);
        return updateQuestion(questionExisted, null);
    }
}
