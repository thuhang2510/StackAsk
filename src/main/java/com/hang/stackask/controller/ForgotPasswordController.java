package com.hang.stackask.controller;

import com.hang.stackask.request.ForgotPasswordRequest;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1")
public class ForgotPasswordController {
    @Autowired
    private IUserService iUserService;

    @PostMapping("/forgot_password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = forgotPasswordRequest.getEmail();
        String siteURL = Utility.getSiteURL(request);

        String result = iUserService.sendMail(email, siteURL);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
