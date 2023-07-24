package com.hang.stackask.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
}
