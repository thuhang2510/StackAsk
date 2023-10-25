package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.ReplyAnswerData;
import com.hang.stackask.request.ReplyAnswerRequest;

public interface IReplyAnswerService {
    ReplyAnswerData create(Long userId, Long answerId, ReplyAnswerRequest replyAnswerRequest);
    ReplyAnswerData update(Long userId, Long replyAnswerId, ReplyAnswerRequest replyAnswerRequest);
    ReplyAnswerData delete(Long userId, Long replyAnswerId);
}
