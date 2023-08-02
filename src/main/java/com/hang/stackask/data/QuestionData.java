package com.hang.stackask.data;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionData {
    private Long id;
    private String uuid;
    private String title;
    private String category;
    private String content;
    private Long vote;
    private Long view;
    private Long userId;
}
