package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface IUserService {
    UserData create(AddUserData data);
    UserData getByEmailAndPassword(String email, String password);
    String sendMail(String email, String siteURL) throws MessagingException, UnsupportedEncodingException;
}
