package com.hang.stackask.data;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private byte[] avatar;
    private boolean enabled;
}
