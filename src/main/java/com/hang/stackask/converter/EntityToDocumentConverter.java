package com.hang.stackask.converter;

import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDocumentConverter {
    @Autowired
    private LocalDateTimeToTypeConverter localDateTimeToTypeConverter;

    public List<QuestionDoc> toDocuments(List<Question> questions){
        List<QuestionDoc> questionDocs = questions.stream()
                .map(question -> {
                    QuestionDoc questionDoc = QuestionDoc.builder()
                            .id(question.getId())
                            .userId(question.getUserId())
                            .content(question.getContent())
                            .enabled(question.getEnabled())
                            .category(question.getCategory())
                            .title(question.getTitle())
                            .uuid(question.getUuid())
                            .createdTime(localDateTimeToTypeConverter.convertLocalDateTimeToLong(question.getCreatedTime()))
                            .build();

                    if(question.getUpdatedTime() != null)
                        questionDoc.setUpdatedTime(localDateTimeToTypeConverter.convertLocalDateTimeToLong(question.getUpdatedTime()));

                    return questionDoc;
                })
                .collect(Collectors.toList());

        return questionDocs;
    }
}
