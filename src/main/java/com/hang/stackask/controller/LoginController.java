package com.hang.stackask.controller;

import com.hang.stackask.data.UserData;
import com.hang.stackask.request.LoginRequest;
import com.hang.stackask.response.AuthenticationResponse;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.JwtUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UserData userData = iUserService.getByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        String token = jwtUtil.generateToken(userResponse);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().accessToken(token).build();

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
