package com.hang.stackask.service.implement;

import com.hang.stackask.converter.StringToTypePrimitiveConverter;
import com.hang.stackask.exception.QuestionNotFoundException;
import com.hang.stackask.response.PageResponse;
import com.hang.stackask.response.PageTokenResponse;
import com.hang.stackask.data.QuestionData;
import com.hang.stackask.entity.Question;
import com.hang.stackask.repository.QuestionRepository;
import com.hang.stackask.request.QuestionRequest;
import com.hang.stackask.service.interfaces.IQuestionService;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.hang.stackask.utils.MapperUtil.mapList;

@Service
public class QuestionServiceImp implements IQuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionData getByUuid(String uuid){
        Question question = questionRepository.getByUuidAndEnabledIsTrue(uuid);

        if(question == null)
            throw new QuestionNotFoundException("question not found");

        QuestionData questionData = modelMapper.map(question, QuestionData.class);
        return questionData;
    }

    @Override
    public QuestionData create(Long userId, QuestionRequest questionRequest) {
        iUserService.getById(userId);

        Question question = modelMapper.map(questionRequest, Question.class);

        String uuid = UUID.randomUUID().toString();
        question.setUuid("Q" + uuid);
        question.setCreatedTime(LocalDateTime.now());
        question.setUserId(userId);

        Question createdQuestion = questionRepository.save(question);

        QuestionData questionData = modelMapper.map(createdQuestion, QuestionData.class);
        return questionData;
    }

    public List<QuestionData> getBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Question> questions = questionRepository.findQuestionOrOrderByCreatedTimeAndId(pageable);

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }

    @Override
    public List<QuestionData> getBooks(String nextPageToken, int limit) throws UnsupportedEncodingException {
        if(StringUtil.isNoContent(nextPageToken))
            return getBooks(limit);

        String decodedNextPageToken = StringUtil.newUtf(Base64.getDecoder().decode(nextPageToken));

        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Question> questions = questionRepository.findQuestions(
                StringToTypePrimitiveConverter.toInt(decodedNextPageToken), pageable);

        List<QuestionData> questionsData = mapList(questions, QuestionData.class);
        return questionsData;
    }
}
