package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.QuestionData;
import com.hang.stackask.request.QuestionRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public interface IQuestionService {
    QuestionData getByUuid(String uuid);
    QuestionData create(Long userId, QuestionRequest questionRequest);
    List<QuestionData> getQuestionsWithPagination(String nextPageToken, int limit) throws UnsupportedEncodingException;
    List<QuestionData> getByTitleOrContentWithPagination(String keyword, String nextPageToken, int limit) throws UnsupportedEncodingException;
    QuestionData update(Long id, QuestionRequest questionRequest);
    QuestionData updateVote(Long id, QuestionRequest questionRequest);
    QuestionData updateView(Long id, QuestionRequest questionRequest);
    List<QuestionData> processGetByTagsInAndCursorAndPageable(Set<String> tagsName, String nextPageToken, int cursor) throws UnsupportedEncodingException;
}
