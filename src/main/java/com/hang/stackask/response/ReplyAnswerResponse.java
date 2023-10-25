package com.hang.stackask.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyAnswerResponse {
    private Long id;
    private String content;
    private Long vote;
    private Long userId;
    private AnswerResponse answer;
}
