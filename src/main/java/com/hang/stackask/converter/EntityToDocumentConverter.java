package com.hang.stackask.converter;

import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityToDocumentConverter {
    @Autowired
    private LocalDateTimeToTypeConverter converter;

    public List<QuestionDoc> toDocuments(List<Question> questions){
        List<QuestionDoc> questionDocs = questions.stream()
                .map(question -> toDocument(question))
                .toList();

        return questionDocs;
    }

    public QuestionDoc toDocument(Question question){
        QuestionDoc questionDoc = QuestionDoc.builder()
                .id(question.getId())
                .userId(question.getUserId())
                .content(question.getContent())
                .enabled(question.getEnabled())
                .category(question.getCategory())
                .title(question.getTitle())
                .uuid(question.getUuid())
                .vote(question.getVote())
                .view(question.getView())
                .createdTime(converter.convertLocalDateTimeToLong(question.getCreatedTime()))
                .build();

        if(question.getUpdatedTime() != null)
            questionDoc.setUpdatedTime(converter.convertLocalDateTimeToLong(question.getUpdatedTime()));

        return questionDoc;
    }
}
