package com.hang.stackask.controller;

import com.hang.stackask.data.UserData;
import com.hang.stackask.exception.UserNotFoundException;
import com.hang.stackask.request.LoginRequest;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.JwtUtil;
import com.hang.stackask.utils.Utility;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService iUserService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private JwtUtil jwtUtil;

    private LoginRequest loginRequest;
    private UserData userData;
    private UserResponse userResponse;

    private final String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxOCIsImlhdCI6MTY4OTM0ODg0MCwiZXh" +
            "wIjoxNjg5NDM1MjQwfQ.M1-JgZ36DLlW3OH7S1J_gAGWZBhdXKzueahj_aNO9Uyaw0Z6wWCrF6yk7fLcn5-McAUSTLgNuETE1jH_Y1qMnA";

    @BeforeEach
    private void init(){
        loginRequest = LoginRequest.builder().email("hangdamthu4@gmail.com").password("123").build();

        userData = UserData.builder()
                .id(1L)
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .password("123")
                .enabled(true)
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .build();
    }

    @Test
    void givenValidLoginRequest_whenLoginIsUsed_thenLoginSuccess() throws Exception {
        given(iUserService.getByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);
        given(jwtUtil.generateToken(userResponse)).willReturn(ACCESS_TOKEN);

        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(loginRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);

        assertEquals(200, status);
        assertEquals(ACCESS_TOKEN, jsonObject.get("accessToken"));
    }

    @Test
    void givenInvalidEmail_whenLoginIsUsed_thenCanNotLogin() throws Exception {
        loginRequest.setEmail("hang.gmail.com");

        given(iUserService.getByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);
        given(jwtUtil.generateToken(userResponse)).willReturn(ACCESS_TOKEN);

        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(loginRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals("Invalid email", content);
    }

    @Test
    void givenPasswordBlank_whenLoginIsUsed_thenCanNotLogin() throws Exception {
        loginRequest.setPassword("");

        given(iUserService.getByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);
        given(jwtUtil.generateToken(userResponse)).willReturn(ACCESS_TOKEN);

        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(loginRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals("Password not blank", content);
    }

    @Test
    void givenGetByEmailAndPasswordThrowsException_whenLoginIsUsed_thenCanNotLogin() throws Exception {
        String messageException = "Exception from method GetByEmailAndPassword";

        given(iUserService.getByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()))
                .willThrow(new UserNotFoundException(messageException));
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);
        given(jwtUtil.generateToken(userResponse)).willReturn(ACCESS_TOKEN);

        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(loginRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(messageException, content);
    }
}
