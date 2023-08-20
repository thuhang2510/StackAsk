package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.AnswerData;
import com.hang.stackask.request.AnswerRequest;

public interface IAnswerService {
    AnswerData create(String uuidQuestion, Long userId, AnswerRequest answerRequest);
    AnswerData update(Long idAnswer, Long userId, AnswerRequest answerRequest);
    AnswerData delete(Long idAnswer, Long userId);
    AnswerData getById(Long idAnswer);
    AnswerData getByUuid(String uuid);
}
