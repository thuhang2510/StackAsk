package com.hang.stackask.request;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyAnswerRequest {
    private String content;
}
