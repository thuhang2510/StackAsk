package com.hang.stackask.mapper.interfaces;

import com.hang.stackask.data.QuestionData;
import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import com.hang.stackask.request.QuestionRequest;

import java.util.List;

public interface IQuestionMapper {
    Question toEntity(QuestionRequest questionRequest);
    QuestionData toData(Question question);
    List<QuestionData> toDatas(List<Question> questions);
    QuestionDoc toDocument(Question question);
    Question toEntity(Question questionExist, QuestionRequest questionRequest);
}
