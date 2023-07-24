package com.hang.stackask.validator;

import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.request.RegisterRequest;

public class RegisterValidator {
    public static void checkPasswordsMatch(RegisterRequest registerRequest){
        if(! registerRequest.getPassword().equals(registerRequest.getRetypePassword()))
            throw new PasswordMismatchException("Password and retype password not match");
    }
}
