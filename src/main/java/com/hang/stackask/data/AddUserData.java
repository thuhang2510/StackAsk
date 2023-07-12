package com.hang.stackask.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddUserData {
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
}
