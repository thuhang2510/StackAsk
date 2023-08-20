package com.hang.stackask.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private Long id;
    private String uuid;
    private String content;
    private Long vote;
    private Long userId;
    private Boolean enabled;
    private QuestionResponse question;
}
