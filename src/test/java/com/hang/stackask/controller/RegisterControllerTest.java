package com.hang.stackask.controller;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;
import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.request.RegisterRequest;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.Utility;
import com.hang.stackask.validator.RegisterValidator;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService iUserService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private RegisterValidator registerValidator;

    private RegisterRequest registerRequest;
    private AddUserData addUserData;
    private UserData userData;
    private UserResponse userResponse;

    @BeforeEach
    private void init(){
        registerRequest = RegisterRequest.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .password("123")
                .retypePassword("123")
                .build();

        addUserData = AddUserData.builder()
                .userName("hang12")
                .fullName("thuhang")
                .email("hangdamthu4@gmail.com")
                .password("123")
                .build();

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
    public void givenValidRegisterRequest_whenRegisterIsUsed_thenRegisterSuccess() throws Exception {
        doNothing().when(registerValidator).checkPasswordsMatch(registerRequest);

        given(modelMapper.map(registerRequest, AddUserData.class)).willReturn(addUserData);
        given(iUserService.create(addUserData)).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);

        MvcResult mvcResult = mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(registerRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);

        assertEquals(200, status);
        assertEquals(userResponse.getId().longValue(), ((Integer) jsonObject.get("id")).longValue());
        assertEquals(userResponse.getUserName(), jsonObject.get("userName"));
        assertEquals(userResponse.getFullName(), jsonObject.get("fullName"));
        assertEquals(userResponse.getEmail(), jsonObject.get("email"));
        assertTrue(jsonObject.isNull("phoneNumber"));
        assertTrue(jsonObject.isNull("avatar"));
    }

    @Test
    public void givenPasswordNotMatch_whenRegisterIsUsed_thenRegisterFail() throws Exception {
        String messageException = "Password and retype password not match";

        registerRequest.setRetypePassword("12345678");

        doThrow(new PasswordMismatchException(messageException)).when(registerValidator).checkPasswordsMatch(registerRequest);

        given(modelMapper.map(registerRequest, AddUserData.class)).willReturn(addUserData);
        given(iUserService.create(addUserData)).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);

        MvcResult mvcResult = mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(registerRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(messageException, content);
    }

    @Test
    public void givenEmailIsBlank_whenRegisterIsUsed_thenRegisterFail() throws Exception {
        registerRequest.setEmail("");

        doNothing().when(registerValidator).checkPasswordsMatch(registerRequest);

        given(modelMapper.map(registerRequest, AddUserData.class)).willReturn(addUserData);
        given(iUserService.create(addUserData)).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);

        MvcResult mvcResult = mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(registerRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals("Email not blank", content);
    }

    @Test
    public void givenInvalidEmail_whenRegisterIsUsed_thenRegisterFail() throws Exception {
        registerRequest.setEmail("12131");

        doNothing().when(registerValidator).checkPasswordsMatch(registerRequest);

        given(modelMapper.map(registerRequest, AddUserData.class)).willReturn(addUserData);
        given(iUserService.create(addUserData)).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);

        MvcResult mvcResult = mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(registerRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals("Email invalid", content);
    }

    @Test
    public void givenUserNameIsBlank_whenRegisterIsUsed_thenRegisterFail() throws Exception {
        registerRequest.setUserName("");

        doNothing().when(registerValidator).checkPasswordsMatch(registerRequest);

        given(modelMapper.map(registerRequest, AddUserData.class)).willReturn(addUserData);
        given(iUserService.create(addUserData)).willReturn(userData);
        given(modelMapper.map(userData, UserResponse.class)).willReturn(userResponse);

        MvcResult mvcResult = mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(registerRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals("User name not blank", content);
    }
}
