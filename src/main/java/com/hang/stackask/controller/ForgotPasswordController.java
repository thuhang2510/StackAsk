package com.hang.stackask.controller;

import com.hang.stackask.request.ForgotPasswordRequest;
import com.hang.stackask.request.ResetPasswordRequest;
import com.hang.stackask.response.SendMailResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.Utility;
import com.hang.stackask.validator.ForgotPasswordValidator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1")
public class ForgotPasswordController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ForgotPasswordValidator forgotPasswordValidator;

    @PostMapping("/forgot_password")
    public ResponseEntity<SendMailResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = forgotPasswordRequest.getEmail();
        String siteURL = Utility.getSiteURL(request);
        String resetPasswordToken = iUserService.forgotPassword(email, siteURL);
        SendMailResponse sendMailResponse = SendMailResponse.builder().resetPasswordToken(resetPasswordToken).build();

        return new ResponseEntity<>(sendMailResponse, HttpStatus.OK);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestParam(name = "token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest){
        forgotPasswordValidator.checkPasswordsMatch(resetPasswordRequest);

        return new ResponseEntity<>(iUserService.resetPassword(token, resetPasswordRequest.getPassword()), HttpStatus.OK);
    }
}
