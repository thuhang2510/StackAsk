package com.hang.stackask.repository;

import com.hang.stackask.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepoTest {
    @Autowired
    private UserRepository userRepo;

    private User userData;

    @BeforeEach
    void setDataUser(){
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
    void givenValidInput_thenCreateUserSuccess() {
        User newUser = userRepo.save(userData);

        assertEquals(userData.getUserName(), newUser.getUserName());
        assertEquals(userData.getFullName(), newUser.getFullName());
        assertEquals(userData.getEmail(), newUser.getEmail());
        assertEquals(userData.getPhoneNumber(), newUser.getPhoneNumber());
        assertEquals(userData.getUserName(), newUser.getUserName());
        assertTrue(newUser.getEnabled());
        assertNull(newUser.getAvatar());
        assertNull(newUser.getResetPasswordToken());
    }

    @Test
    void givenInvalidUserName_whenSaveUser_thenThrowsException(){
        userData.setUserName("");
        assertThrows(ConstraintViolationException.class, () -> userRepo.save(userData));
    }

    @Test
    void givenInvalidEmail_whenSaveUser_thenThrowsException(){
        userData.setEmail("");
        assertThrows(ConstraintViolationException.class, () -> userRepo.save(userData));
    }

    @Test
    void givenInvalidPassword_whenSaveUser_thenThrowsException(){
        userData.setPassword("");
        assertThrows(ConstraintViolationException.class, () -> userRepo.save(userData));
    }

    @Test
    void givenEmailExistAndEnabledTrue_whenSaveUser_thenThrowsException(){
        initUser(userData);

        assertThrows(RuntimeException.class, () -> userRepo.save(userData));
    }

    @Test
    void givenPhoneDuplicatedAndEnabledTrue_whenSaveUser_thenThrowsException(){
        initUser(userData);

        userData.setEmail("newEmail@gmail.com");

        assertThrows(RuntimeException.class, () -> userRepo.save(userData));
    }

    @Test
    void givenEmailCorrect_whenGetUserByEmailAndPasswordAndEnabledTrue_thenGetUserSuccess(){
        User user = initUser(userData);

        User existUser = userRepo.getUserByEmailAndEnabledIsTrue(user.getEmail());

        assertEquals(userData.getUserName(), existUser.getUserName());
        assertEquals(userData.getFullName(), existUser.getFullName());
        assertEquals(userData.getEmail(), existUser.getEmail());
        assertEquals(userData.getPhoneNumber(), existUser.getPhoneNumber());
        assertEquals(userData.getUserName(), existUser.getUserName());
        assertTrue(existUser.getEnabled());
    }

    @Test
    void givenEmailWrong_whenGetUserByEmailAndPasswordAndEnabledTrue_thenReturnNull(){
        initUser(userData);

        User existUser = userRepo.getUserByEmailAndEnabledIsTrue("1234");
        assertNull(existUser);
    }

    @Test
    void givenExistedUser_whenUpdateResetPassword_thenUpdateSuccess(){
        User user = initUser(userData);
        assertNotNull(user);

        user.setResetPasswordToken("new ResetPassword");
        User updatedUser = userRepo.save(user);
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.getId(), updatedUser.getId());
    }
}