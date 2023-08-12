package com.hang.stackask.data;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Builder.Default
    private Set<String> tags = new HashSet<>();
}
