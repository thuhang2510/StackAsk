package com.hang.stackask.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse {
    private Long id;
    private String uuid;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private Long uploadedBy;
    private String parentUuid;
}
