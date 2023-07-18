package com.hang.stackask.service;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.entity.User;
import com.hang.stackask.exception.UserNotFoundException;
import com.hang.stackask.repository.UserRepository;
import com.hang.stackask.service.implement.UserServiceImp;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.EmailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @TestConfiguration
    public static class TestUserServiceConfiguration{
        @Bean
        UserServiceImp userServiceImp(){
            return new UserServiceImp();
        }
    }

    @MockBean
    UserRepository userRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private IUserService iUserService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailUtil emailUtil;

    private AddUserData addUserData;
    private User userEntity;
    private User convertDataToEntity;
    private UserData userData;
    private String PASSWORD_ENCODER = "$2a$10$UfxGxMURPZsfEwvaIieSeOZuPoYeDszGYQcCASDNIPpfQJBgtHC0e";

    @BeforeEach
    public void init(){
        addUserData = AddUserData.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password("thuhang2001")
                .build();

        convertDataToEntity = User.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password("thuhang2001")
                .build();

        userEntity = User.builder()
                .id(1L)
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password(PASSWORD_ENCODER)
                .createdTime(LocalDateTime.now())
                .build();

        userData = UserData.builder()
                .id(1L)
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password(PASSWORD_ENCODER)
                .enabled(true)
                .build();
    }

    @Test
    void givenValidAddUserData_whenCreateUser_thenCreateUserSuccess(){
        given(passwordEncoder.encode(addUserData.getPassword())).willReturn(PASSWORD_ENCODER);
        given(userRepository.save(convertDataToEntity)).willReturn(userEntity);

        given(modelMapper.map(addUserData, User.class)).willReturn(convertDataToEntity);
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        UserData actualUser = iUserService.create(addUserData);

        assertSame(userData, actualUser);
    }

    @Test
    void givenMethodSaveThrowsException_whenCreateUser_thenThrowsException(){
        given(passwordEncoder.encode(addUserData.getPassword())).willReturn(PASSWORD_ENCODER);

        given(userRepository.save(convertDataToEntity)).willThrow(new RuntimeException("Exception"));
        given(modelMapper.map(addUserData, User.class)).willReturn(convertDataToEntity);

        assertThrows(RuntimeException.class, () -> iUserService.create(addUserData));
    }

    @Test
    void givenCanNotMapDataToEntity_whenCreateUser_thenThrowsMappingException(){
        given(passwordEncoder.encode(addUserData.getPassword())).willReturn(PASSWORD_ENCODER);

        given(userRepository.save(convertDataToEntity)).willReturn(userEntity);
        given(modelMapper.map(addUserData, User.class)).willThrow(new MappingException(new ArrayList<>()));

        assertThrows(MappingException.class, () -> iUserService.create(addUserData));
    }

    @Test
    void givenUserNotSaveInDbAndCanNotMapEntityToData_whenCreateUser_thenThrowsMappingException(){
        given(passwordEncoder.encode(addUserData.getPassword())).willReturn(PASSWORD_ENCODER);
        given(userRepository.save(convertDataToEntity)).willReturn(null);

        given(modelMapper.map(addUserData, User.class)).willReturn(convertDataToEntity);
        given(modelMapper.map(null, UserData.class)).willThrow(new MappingException(new ArrayList<>()));

        assertThrows(MappingException.class, () -> iUserService.create(addUserData));
    }

    @Test
    void givenEmailAndPasswordCorrect_whenGetUserByEmailAndPassword_thenGetUserSuccess(){
        String email = userData.getEmail();
        String password = userData.getPassword();

        given(passwordEncoder.matches(password, PASSWORD_ENCODER)).willReturn(true);
        given(userRepository.getUserByEmailAndEnabledIsTrue(email)).willReturn(userEntity);
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        UserData actualUser = iUserService.getByEmailAndPassword(email, password);
        assertSame(userData, actualUser);
    }

    @Test
    void givenEmailAndPasswordWrong_whenGetUserByEmailAndPassword_thenThrowsUserNotFoundException(){
        String email = userData.getEmail();
        String password = userData.getPassword();

        given(passwordEncoder.matches(password, PASSWORD_ENCODER)).willReturn(true);
        given(userRepository.getUserByEmailAndEnabledIsTrue(email)).willReturn(null);
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        assertThrows(UserNotFoundException.class, () -> iUserService.getByEmailAndPassword(email, password));
    }

    @Test
    void givenGetInRepositoryThrowsException_whenGetUserByEmailAndPassword_thenThrowsException(){
        String email = userData.getEmail();
        String password = userData.getPassword();

        given(passwordEncoder.matches(password, PASSWORD_ENCODER)).willReturn(true);
        given(userRepository.getUserByEmailAndEnabledIsTrue(email))
                .willThrow(new RuntimeException("exception"));
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        assertThrows(RuntimeException.class, () -> iUserService.getByEmailAndPassword(email, password));
    }

    @Test
    void givenMappingException_whenGetUserByEmailAndPassword_thenThrowsException(){
        String email = userData.getEmail();
        String password = userData.getPassword();

        given(passwordEncoder.matches(password, PASSWORD_ENCODER)).willReturn(true);
        given(userRepository.getUserByEmailAndEnabledIsTrue(email)).willReturn(userEntity);
        given(modelMapper.map(userEntity, UserData.class)).willThrow(new MappingException(new ArrayList<>()));

        assertThrows(MappingException.class, () -> iUserService.getByEmailAndPassword(email, password));
    }

    @Test
    void givenPasswordIsWrong_whenGetUserByEmailAndPassword_thenThrowsUserNotFoundException(){
        String email = userData.getEmail();
        String password = userData.getPassword();

        given(passwordEncoder.matches(password, PASSWORD_ENCODER)).willReturn(false);
        given(userRepository.getUserByEmailAndEnabledIsTrue(email)).willReturn(userEntity);
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        assertThrows(UserNotFoundException.class, () -> iUserService.getByEmailAndPassword(email, password));
    }
}
