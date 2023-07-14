package com.hang.stackask.service.implement;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.entity.User;
import com.hang.stackask.exception.UserNotFoundException;
import com.hang.stackask.repository.UserRepository;
import com.hang.stackask.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserData create(AddUserData data) {
        User userEntity = modelMapper.map(data, User.class);
        userEntity.setCreatedTime(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(data.getPassword()));

        userEntity = userRepository.save(userEntity);

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
}
