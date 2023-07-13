package com.hang.stackask.controller;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.request.RegisterRequest;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.validator.RegisterValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RegisterValidator registerValidator;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        registerValidator.checkPasswordsMatch(registerRequest);

        final AddUserData addUserData = modelMapper.map(registerRequest, AddUserData.class);
        final UserData userData = iUserService.create(addUserData);
        final UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return userResponse;
    }
}
