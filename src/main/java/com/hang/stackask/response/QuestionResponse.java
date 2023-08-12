package com.hang.stackask.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private Long id;
    private String uuid;
    private String title;
    private String category;
    private String content;
    private Long vote;
    private Long view;
    private Long userId;

    @Builder.Default
    private Set<String> tags = new HashSet<>();
}
