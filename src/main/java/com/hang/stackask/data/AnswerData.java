package com.hang.stackask.data;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerData {
    private Long id;
    private String content;
    private String uuid;
    private Long vote;
    private Long userId;
    private QuestionData questionData;
}
