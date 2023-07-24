package com.hang.stackask.validator;

import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.request.ChangePasswordRequest;

public class ChangePasswordValidator {
    public static void checkPasswordsMatch(ChangePasswordRequest changePasswordRequest){
        if(! changePasswordRequest.getNewPassword().equals(changePasswordRequest.getRetypeNewPassword()))
            throw new PasswordMismatchException("New password and retype new password not match");
    }
}
