package com.hang.stackask.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
