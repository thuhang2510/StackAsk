package com.hang.stackask.repository;

import com.hang.stackask.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepoTest {
    @Autowired
    private UserRepository userRepo;

    private User userData;

    @BeforeEach
    public void setDataUser(){
        userData = User.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password("thuhang2001")
                .createdTime(LocalDateTime.now())
                .build();
    }

    private User initUser(User user){
        User userInfo = User.builder()
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .createdTime(LocalDateTime.now())
                .build();

        return userRepo.save(userInfo);
    }

    @Test
    public void givenValidInput_thenCreateUserSuccess() {
        User newUser = userRepo.save(userData);

        assertEquals(userData.getUserName(), newUser.getUserName());
        assertEquals(userData.getFullName(), newUser.getFullName());
        assertEquals(userData.getEmail(), newUser.getEmail());
        assertEquals(userData.getPhoneNumber(), newUser.getPhoneNumber());
        assertEquals(userData.getUserName(), newUser.getUserName());
        assertEquals(userData.getEnabled(), newUser.getEnabled());
        assertNull(newUser.getAvatar());
        assertNull(newUser.getResetPasswordToken());
    }
}