package com.hang.stackask.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String retypeNewPassword;
}
