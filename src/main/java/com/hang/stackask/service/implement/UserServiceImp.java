package com.hang.stackask.service.implement;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UpdateAccountData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.entity.User;
import com.hang.stackask.exception.FailedToUpdateUserException;
import com.hang.stackask.exception.UserAlreadyExistException;
import com.hang.stackask.exception.UserNotFoundException;
import com.hang.stackask.repository.UserRepository;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.service.interfaces.IVerificationTokenService;
import com.hang.stackask.utils.EmailUtil;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.hang.stackask.utils.MapperUtil.mapList;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private IVerificationTokenService iVerificationTokenService;

    private User create(AddUserData data) {
        User userEntity = modelMapper.map(data, User.class);
        User existedUser = userRepository.getUserByEmailAndEnabledIsTrue(userEntity.getEmail());

        if(existedUser != null)
            throw new UserAlreadyExistException("There is an account with that email address: " + userEntity.getEmail());

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setCreatedTime(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(data.getPassword()));

        return userRepository.save(userEntity);
    }

    @Override
    public UserData create(AddUserData data, String siteURL) throws MessagingException, UnsupportedEncodingException {
        User userEntity = create(data);

        String token = iVerificationTokenService.create(userEntity.getId());
        sendRegistrationConfirmMail(userEntity.getEmail(), siteURL, token);

        UserData userData = modelMapper.map(userEntity, UserData.class);
        return userData;
    }

    private void sendRegistrationConfirmMail(String email, String siteURL, String token) throws MessagingException, UnsupportedEncodingException {
        String confirmLink = siteURL + "/registrationConfirm?token=" + token;

        final String subject = "Registration Confirmation";
        final String content = "You registered successfully. To confirm your registration, please click on the below link.\n"
                + "<a href=\"" + confirmLink + "\"> Change password</a>" + "";

        emailUtil.sendMail(email, subject, content);
    }

    @Override
    public UserData confirm(Long userId){
        User existedUser = userRepository.getReferenceById(userId);

        if (existedUser == null)
            throw new UserNotFoundException("user not found");

        existedUser.setEnabled(true);
        User updatedUser = userRepository.save(existedUser);

        UserData userData = modelMapper.map(updatedUser, UserData.class);

        return userData;
    }

    @Override
    public UserData getById(Long userId) {
        User userEntity = userRepository.getReferenceById(userId);

        if(userEntity == null)
            throw new UserNotFoundException("user not found");

        UserData userData = modelMapper.map(userEntity, UserData.class);

        return userData;
    }

    @Override
    public List<UserData> getAll() {
        List<User> users = userRepository.getAllByEnabledIsTrue();
        List<UserData> userDatas = mapList(users, UserData.class);

        return userDatas;
    }

    @Override
    public UserData updateAvatar(Long userId, MultipartFile file) throws IOException {
        User userEntity = userRepository.getReferenceById(userId);

        if(userEntity == null)
            throw new UserNotFoundException("user not found");

        userEntity.setAvatar(file.getBytes());
        userEntity.setUpdatedTime(LocalDateTime.now());
        userRepository.save(userEntity);

        UserData userData = modelMapper.map(userEntity, UserData.class);
        return userData;
    }

    @Override
    public UserData updatePassword(Long userId, String nPassword) {
        User userEntity = userRepository.getReferenceById(userId);

        if(userEntity == null)
            throw new UserNotFoundException("user not found");

        userEntity.setPassword(passwordEncoder.encode(nPassword));
        userEntity.setUpdatedTime(LocalDateTime.now());
        userRepository.save(userEntity);

        UserData userData = modelMapper.map(userEntity, UserData.class);
        return userData;
    }

    @Override
    public UserData update(Long userId, UpdateAccountData updateAccountData){
        User userEntity = userRepository.getReferenceById(userId);

        if(userEntity == null)
            throw new UserNotFoundException("user not found");

        userEntity.setFullName(updateAccountData.getFullName());
        userEntity.setEmail(updateAccountData.getEmail());
        userEntity.setPhoneNumber(updateAccountData.getPhoneNumber());
        userEntity.setUpdatedTime(LocalDateTime.now());

        userRepository.save(userEntity);

        UserData userData = modelMapper.map(userEntity, UserData.class);
        return userData;
    }

    @Override
    public UserData getByUuid(String uuid) {
        User userEntity = userRepository.getUserByUuidAndEnabledIsTrue(uuid);

        if(userEntity == null)
            throw new UserNotFoundException("user not found");

        UserData userData = modelMapper.map(userEntity, UserData.class);
        return userData;
    }

    @Override
    public UserData getByEmailAndPassword(String email, String password) {
        User existedBook = userRepository.getUserByEmailAndEnabledIsTrue(email);

        if(existedBook == null)
            throw new UserNotFoundException("not found user with email: " + email);

        if(!passwordEncoder.matches(password, existedBook.getPassword()))
            throw new UserNotFoundException("password is wrong: " + password);

        UserData userData = modelMapper.map(existedBook, UserData.class);

        return userData;
    }

    private User updateResetPassword(String token, String email){
        User existedUser = userRepository.getUserByEmailAndEnabledIsTrue(email);

        if (existedUser == null)
            throw new UserNotFoundException("User not exist");

        existedUser.setResetPasswordToken(token);
        existedUser.setUpdatedTime(LocalDateTime.now());

        return userRepository.save(existedUser);
    }

    @Override
    public String forgotPassword(String email, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String token = RandomString.make(55);
        updateResetPassword(token, email);
        sendResetPasswordMail(email, siteURL, token);

        return token;
    }

    private void sendResetPasswordMail(String email, String siteURL, String token) throws MessagingException, UnsupportedEncodingException {
        String resetPasswordLink = siteURL + "/registrationConfirm?token=" + token;

        final String subject = "Here's the link to reset your password";
        final String content = "Hello, You have requested to reset your password."
                + "Click the link below to change your password"
                + "<a href=\"" + resetPasswordLink + "\"> Change password</a>" + "";

        emailUtil.sendMail(email, subject, content);
    }

    @Override
    public String resetPassword(String resetPasswordToken, String nPassword) {
        User existedUser = userRepository.getUserByResetPasswordTokenAndEnabledIsTrue(resetPasswordToken);

        if (existedUser == null)
            throw new UserNotFoundException("user does not ask for password reset");

        existedUser.setPassword(passwordEncoder.encode(nPassword));
        existedUser.setResetPasswordToken(null);

        User updatedUser = userRepository.save(existedUser);

        if(updatedUser == null)
            throw new FailedToUpdateUserException("failed to update password");

        return "update password success";
    }
}
