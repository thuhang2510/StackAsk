package com.hang.stackask.validator;

import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.request.ResetPasswordRequest;

public class ForgotPasswordValidator {
    public static void checkPasswordsMatch(ResetPasswordRequest resetPasswordRequest){
        if(! resetPasswordRequest.getPassword().equals(resetPasswordRequest.getRetypePassword()))
            throw new PasswordMismatchException("Password and retype password not match");
    }
}
