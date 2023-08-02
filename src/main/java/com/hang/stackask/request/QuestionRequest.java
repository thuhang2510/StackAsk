package com.hang.stackask.request;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private String title;
    private String category;
    private String content;
}
