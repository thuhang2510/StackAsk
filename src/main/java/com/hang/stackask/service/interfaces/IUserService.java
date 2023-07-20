package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface IUserService {
    UserData create(AddUserData data, String siteURL) throws MessagingException, UnsupportedEncodingException;
    UserData getByEmailAndPassword(String email, String password);
    String forgotPassword(String email, String siteURL) throws MessagingException, UnsupportedEncodingException;
    String resetPassword(String resetPasswordToken, String nPassword);
    UserData confirm(Long userId);
}
