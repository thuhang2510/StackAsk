package com.hang.stackask.service.implement;

import com.hang.stackask.data.AnswerData;
import com.hang.stackask.data.ReplyAnswerData;
import com.hang.stackask.entity.Answer;
import com.hang.stackask.entity.ReplyAnswer;
import com.hang.stackask.exception.NoUpdatePermissionException;
import com.hang.stackask.exception.ReplyAnswerNotFoundException;
import com.hang.stackask.repository.ReplyAnswerRepository;
import com.hang.stackask.request.ReplyAnswerRequest;
import com.hang.stackask.service.interfaces.IAnswerService;
import com.hang.stackask.service.interfaces.IReplyAnswerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReplyAnswerServiceImp implements IReplyAnswerService {
    @Autowired
    private ReplyAnswerRepository replyAnswerRepository;

    @Autowired
    private IAnswerService iAnswerService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReplyAnswerData create(Long userId, Long answerId, ReplyAnswerRequest replyAnswerRequest) {
        AnswerData answerData = iAnswerService.getById(answerId);

        ReplyAnswer replyAnswer = modelMapper.map(replyAnswerRequest, ReplyAnswer.class);
        replyAnswer.setAnswer(modelMapper.map(answerData, Answer.class));
        replyAnswer.setUserId(userId);
        replyAnswer.setCreatedTime(LocalDateTime.now());

        replyAnswerRepository.save(replyAnswer);

        return modelMapper.map(replyAnswer, ReplyAnswerData.class);
    }

    private ReplyAnswer findReplyAnswerById(Long replyAnswerId){
        ReplyAnswer replyAnswer = replyAnswerRepository.getByIdAndEnabledIsTrue(replyAnswerId);

        if (replyAnswer == null)
            throw new ReplyAnswerNotFoundException("reply answer not found");

        return replyAnswer;
    }

    @Override
    public ReplyAnswerData update(Long userId, Long replyAnswerId, ReplyAnswerRequest replyAnswerRequest) {
        ReplyAnswer replyAnswer = findReplyAnswerById(replyAnswerId);

        if(! userId.equals(replyAnswer.getUserId()))
            throw new NoUpdatePermissionException("You do not have permission to update");

        replyAnswer.setContent(replyAnswerRequest.getContent());
        replyAnswer.setUpdatedTime(LocalDateTime.now());
        replyAnswerRepository.save(replyAnswer);

        return modelMapper.map(replyAnswer, ReplyAnswerData.class);
    }

    @Override
    public ReplyAnswerData delete(Long userId, Long replyAnswerId) {
        ReplyAnswer replyAnswer = findReplyAnswerById(replyAnswerId);

        if(! userId.equals(replyAnswer.getUserId()))
            throw new NoUpdatePermissionException("You do not have permission to delete");

        replyAnswer.setEnabled(false);
        replyAnswer.setUpdatedTime(LocalDateTime.now());
        replyAnswerRepository.save(replyAnswer);

        return modelMapper.map(replyAnswer, ReplyAnswerData.class);
    }
}
