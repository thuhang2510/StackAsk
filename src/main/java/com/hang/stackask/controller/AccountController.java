package com.hang.stackask.controller;

import com.hang.stackask.data.UpdateAccountData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.exception.NotLoggedInException;
import com.hang.stackask.request.ChangePasswordRequest;
import com.hang.stackask.request.UpdateAccountRequest;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.JwtFilterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.hang.stackask.validator.ChangePasswordValidator.checkPasswordsMatch;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtFilterUtil jwtFilterUtil;

    public static final String NOT_LOGGED_IN = "not logged in";

    @GetMapping("/")
    public ResponseEntity<UserResponse> getById(@RequestHeader("Authorization") String authHeader){
        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        UserData userData = iUserService.getById(userId);
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/update_avatar")
    public ResponseEntity<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) throws IOException {
        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        UserData userData = iUserService.updateAvatar(userId, file);
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/avatar", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getAvatar(@RequestHeader("Authorization") String authHeader){
        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        return iUserService.getById(userId).getAvatar();
    }

    @PutMapping("/update_password")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @RequestHeader("Authorization") String authHeader){
        checkPasswordsMatch(changePasswordRequest);

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        UserData userData = iUserService.updatePassword(userId, changePasswordRequest.getNewPassword());
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> update(@RequestBody UpdateAccountRequest updateAccountRequest, @RequestHeader("Authorization") String authHeader){
        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        UpdateAccountData updateAccountData = modelMapper.map(updateAccountRequest, UpdateAccountData.class);
        UserData userData = iUserService.update(userId, updateAccountData);
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
