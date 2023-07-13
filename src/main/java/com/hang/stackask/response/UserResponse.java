package com.hang.stackask.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private byte[] avatar;
}
