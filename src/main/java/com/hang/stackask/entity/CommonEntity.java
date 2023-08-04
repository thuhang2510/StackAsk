package com.hang.stackask.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class CommonEntity {
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime updatedTime;
}
