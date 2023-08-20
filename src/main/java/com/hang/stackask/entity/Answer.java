package com.hang.stackask.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_answer")
public class Answer extends CommonEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Builder.Default
    private Long vote = 0L;

    @Column(name = "user_id")
    private Long userId;

    private String uuid;

    @Builder.Default
    private Boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Question question;
}
