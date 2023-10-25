package com.hang.stackask.data;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyAnswerData {
    private Long id;
    private String content;
    private String uuid;
    private Long vote;
    private Long userId;
    private AnswerData answerData;
}
