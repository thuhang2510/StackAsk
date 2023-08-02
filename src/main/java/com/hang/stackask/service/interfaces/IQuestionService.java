package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.QuestionData;
import com.hang.stackask.request.QuestionRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IQuestionService {
    QuestionData getByUuid(String uuid);
    QuestionData create(Long userId, QuestionRequest questionRequest);
    List<QuestionData> getBooks(String nextPageToken, int limit) throws UnsupportedEncodingException;
}
