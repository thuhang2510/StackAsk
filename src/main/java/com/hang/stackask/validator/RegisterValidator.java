package com.hang.stackask.validator;

import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.request.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidator {
    public void checkPasswordsMatch(RegisterRequest registerRequest){
        if(! registerRequest.getPassword().equals(registerRequest.getRetypePassword()))
            throw new PasswordMismatchException("Password and retype password not match");
    }
}
