package com.hang.stackask.service.implement;

import com.hang.stackask.data.AnswerData;
import com.hang.stackask.entity.Answer;
import com.hang.stackask.entity.Question;
import com.hang.stackask.exception.AnswerNotFoundExcepyion;
import com.hang.stackask.exception.NoDeletePermissionException;
import com.hang.stackask.exception.NoUpdatePermissionException;
import com.hang.stackask.repository.AnswerRepository;
import com.hang.stackask.repository.QuestionRepository;
import com.hang.stackask.request.AnswerRequest;
import com.hang.stackask.service.interfaces.IAnswerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AnswerServiceImp implements IAnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AnswerData create(String uuidQuestion, Long userId, AnswerRequest answerRequest) {
        Question question = questionRepository.getByUuidAndEnabledIsTrue(uuidQuestion);

        Answer answer = modelMapper.map(answerRequest, Answer.class);

        String uuid = UUID.randomUUID().toString();
        answer.setUuid("A" + uuid);
        answer.setUserId(userId);
        answer.setQuestion(question);

        answerRepository.save(answer);

        return modelMapper.map(answer, AnswerData.class);
    }

    private Answer findAnswerById(Long idAnswer){
        Answer answer = answerRepository.getByIdAndEnabledIsTrue(idAnswer);

        if(answer == null)
            throw new AnswerNotFoundExcepyion("answer not exist");

        return answer;
    }

    @Override
    public AnswerData getById(Long idAnswer){
        Answer answer = findAnswerById(idAnswer);
        return modelMapper.map(answer, AnswerData.class);
    }

    @Override
    public AnswerData update(Long idAnswer, Long userId, AnswerRequest answerRequest){
        Answer answer = findAnswerById(idAnswer);

        if(! userId.equals(answer.getUserId()))
            throw new NoUpdatePermissionException("You do not have permission to update");

        answer.setContent(answerRequest.getContent());
        answer.setUpdatedTime(LocalDateTime.now());
        answerRepository.save(answer);

        return modelMapper.map(answer, AnswerData.class);
    }

    @Override
    public AnswerData delete(Long idAnswer, Long userId){
        Answer answer = findAnswerById(idAnswer);

        if(! userId.equals(answer.getUserId()))
            throw new NoDeletePermissionException("You do not have permission to delete");

        answer.setEnabled(false);
        answer.setUpdatedTime(LocalDateTime.now());
        answerRepository.save(answer);

        return modelMapper.map(answer, AnswerData.class);
    }

    @Override
    public AnswerData getByUuid(String uuid) {
        Answer answer = answerRepository.getByUuidAndEnabledIsTrue(uuid);

        if(answer == null)
            throw new AnswerNotFoundExcepyion("answer not exist");

        return modelMapper.map(answer, AnswerData.class);
    }
}
