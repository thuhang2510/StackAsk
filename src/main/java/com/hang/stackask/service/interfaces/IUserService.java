package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UpdateAccountData;
import com.hang.stackask.data.UserData;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService {
    UserData create(AddUserData data, String siteURL) throws MessagingException, UnsupportedEncodingException;
    UserData getByEmailAndPassword(String email, String password);
    String forgotPassword(String email, String siteURL) throws MessagingException, UnsupportedEncodingException;
    String resetPassword(String resetPasswordToken, String nPassword);
    UserData confirm(Long userId);
    UserData getById(Long userId);
    List<UserData> getAll();
    UserData updateAvatar(Long userId, MultipartFile file) throws IOException;
    UserData updatePassword(Long userId, String nPassword);
    UserData update(Long userId, UpdateAccountData updateAccountData);
    UserData getByUuid(String uuid);
}
