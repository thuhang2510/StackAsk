package com.hang.stackask.controller;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.data.VerificationTokenData;
import com.hang.stackask.exception.VerificationTokenTimeExpiredException;
import com.hang.stackask.request.RegisterRequest;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.service.interfaces.IVerificationTokenService;
import com.hang.stackask.utils.Utility;
import com.hang.stackask.validator.RegisterValidator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private IVerificationTokenService iVerificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        registerValidator.checkPasswordsMatch(registerRequest);
        String siteURL = Utility.getSiteURL(request);

        final AddUserData addUserData = modelMapper.map(registerRequest, AddUserData.class);
        final UserData userData = iUserService.create(addUserData, siteURL);
        final UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<String> registrationConfirm(@RequestParam(name = "token") String token){
        VerificationTokenData verificationTokenData = iVerificationTokenService.getByToken(token);

        Calendar cal = Calendar.getInstance();
        if ((verificationTokenData.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            throw new VerificationTokenTimeExpiredException("verification token invalid");

        iUserService.confirm(verificationTokenData.getUserId());

        return new ResponseEntity<>("Confirm Success", HttpStatus.OK);
    }
}
