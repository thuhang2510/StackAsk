package com.hang.stackask.request;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private Set<String> tags = new HashSet<>();
}
