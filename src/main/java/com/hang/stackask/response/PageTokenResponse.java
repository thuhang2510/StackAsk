package com.hang.stackask.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageTokenResponse {
    private String next;
    private String prev;
}
