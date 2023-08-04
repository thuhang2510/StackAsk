package com.hang.stackask.document;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "question_stack_ask")
public class QuestionDoc extends CommonDoc{
    @Id
    private Long id;
    private String uuid;
    private String title;
    private String category;
    private String content;
    @Builder.Default
    private Long vote = 0L;
    @Builder.Default
    private Long view = 0L;
    @Field(name = "user_id")
    private Long userId;
    @Builder.Default
    private Boolean enabled = true;
}
