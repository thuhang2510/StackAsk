package com.hang.stackask.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private T items;
    private PageTokenResponse pageTokenResponse;
    private int count;
}
