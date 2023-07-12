package com.hang.stackask.service;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.entity.User;
import com.hang.stackask.repository.UserRepository;
import com.hang.stackask.service.implement.UserServiceImp;
import com.hang.stackask.service.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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

    private AddUserData addUserData;
    private User userEntity;
    private User convertAddUserDataToEntity;
    private UserData userData;

    @BeforeEach
    public void init(){
        addUserData = AddUserData.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password("thuhang2001")
                .build();

        convertAddUserDataToEntity = User.builder()
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
                .password("thuhang2001")
                .createdTime(LocalDateTime.now())
                .build();

        userData = UserData.builder()
                .id(1L)
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .phoneNumber("0389682304")
                .password("thuhang2001")
                .enabled(true)
                .build();
    }

    @Test
    void givenValidAddUserData_whenCreateUser_thenCreateUserSuccess(){
        given(userRepository.save(convertAddUserDataToEntity)).willReturn(userEntity);

        given(modelMapper.map(addUserData, User.class)).willReturn(convertAddUserDataToEntity);
        given(modelMapper.map(userEntity, UserData.class)).willReturn(userData);

        UserData actualUser = iUserService.create(addUserData);

        assertSame(userData, actualUser);
    }

    @Test
    void givenMethodSaveThrowsException_whenCreateUser_thenThrowsException(){
        given(userRepository.save(convertAddUserDataToEntity)).willThrow(new RuntimeException("Exception"));
        given(modelMapper.map(addUserData, User.class)).willReturn(convertAddUserDataToEntity);

        assertThrows(RuntimeException.class, () -> iUserService.create(addUserData));
    }

    @Test
    void givenCanNotMapDataToEntity_whenCreateUser_thenThrowsMappingException(){
        given(userRepository.save(convertAddUserDataToEntity)).willReturn(userEntity);
        given(modelMapper.map(addUserData, User.class)).willThrow(new MappingException(new ArrayList<>()));

        assertThrows(MappingException.class, () -> iUserService.create(addUserData));
    }

    @Test
    void givenUserNotSaveInDbAndCanNotMapEntityToData_whenCreateUser_thenThrowsMappingException(){
        given(userRepository.save(convertAddUserDataToEntity)).willReturn(null);

        given(modelMapper.map(addUserData, User.class)).willReturn(convertAddUserDataToEntity);
        given(modelMapper.map(null, UserData.class)).willThrow(new MappingException(new ArrayList<>()));

        assertThrows(MappingException.class, () -> iUserService.create(addUserData));
    }
}
