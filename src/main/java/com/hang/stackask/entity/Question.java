package com.hang.stackask.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_question")
public class Question extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String title;
    private String category;
    private String content;

    @Builder.Default
    private Long vote = 0L;

    @Builder.Default
    private Long view = 0L;

    @Column(name = "user_id")
    private Long userId;

    @Builder.Default
    private Boolean enabled = true;
}
