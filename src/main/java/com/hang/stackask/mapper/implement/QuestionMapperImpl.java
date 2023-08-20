package com.hang.stackask.mapper.implement;

import com.hang.stackask.converter.LocalDateTimeToTypeConverter;
import com.hang.stackask.data.QuestionData;
import com.hang.stackask.document.QuestionDoc;
import com.hang.stackask.entity.Question;
import com.hang.stackask.entity.Tag;
import com.hang.stackask.mapper.interfaces.IQuestionMapper;
import com.hang.stackask.request.QuestionRequest;
import com.hang.stackask.service.interfaces.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapperImpl implements IQuestionMapper {
    @Autowired
    private ITagService iTagService;

    @Autowired
    private LocalDateTimeToTypeConverter converter;

    @Override
    public Question toEntity(QuestionRequest questionRequest) {
        Question question = new Question();

        question.setTitle(questionRequest.getTitle());
        question.setCategory(questionRequest.getCategory());
        question.setContent(questionRequest.getContent());

        if (question.getTags() == null) {
            question.setTags(new HashSet<>());
        }

        questionRequest.getTags().stream().forEach(tagName -> {
            Tag tag = iTagService.getOrCreatTag(tagName);
            question.addTag(tag);
        });

        return question;
    }

    @Override
    public Question toEntity(Question questionExist, QuestionRequest questionRequest) {
        questionExist.setTitle(questionRequest.getTitle());
        questionExist.setCategory(questionRequest.getCategory());
        questionExist.setContent(questionRequest.getContent());

        if (questionExist.getTags() == null) {
            questionExist.setTags(new HashSet<>());
        }

        questionRequest.getTags().stream().forEach(tagName -> {
            Tag tag = iTagService.getOrCreatTag(tagName);
            questionExist.addTag(tag);
        });

        return questionExist;
    }

    @Override
    public List<QuestionData> toDatas(List<Question> questions){
        List<QuestionData> questionsData = questions.stream()
                .map(this::toData)
                .toList();

        return questionsData;
    }

    @Override
    public QuestionData toData(Question question){
        QuestionData questionData = QuestionData.builder()
                .id(question.getId())
                .userId(question.getUserId())
                .content(question.getContent())
                .category(question.getCategory())
                .title(question.getTitle())
                .uuid(question.getUuid())
                .vote(question.getVote())
                .view(question.getView())
                .build();

        if(question.getTags().size() > 0){
            questionData.setTags(question.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet()));
        }

        return questionData;
    }

    @Override
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

        if(question.getTags().size() > 0) {
            questionDoc.setTags(question.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet()));
        }

        return questionDoc;
    }
}
