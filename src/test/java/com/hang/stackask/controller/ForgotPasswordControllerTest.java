package com.hang.stackask.controller;

import com.hang.stackask.exception.PasswordMismatchException;
import com.hang.stackask.exception.UserNotFoundException;
import com.hang.stackask.request.ForgotPasswordRequest;
import com.hang.stackask.request.ResetPasswordRequest;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.Utility;
import com.hang.stackask.validator.ForgotPasswordValidator;
import jakarta.mail.MessagingException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ForgotPasswordController.class)
public class ForgotPasswordControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService iUserService;

    @MockBean
    private ForgotPasswordValidator forgotPasswordValidator;

    private ForgotPasswordRequest forgotPasswordRequest;
    private ResetPasswordRequest resetPasswordRequest;

    private final String EMAIL = "hangdamthu4@gmail.com";
    private final String SITE_URL = "http://localhost/api/v1";

    @BeforeEach
    void init(){
        forgotPasswordRequest = ForgotPasswordRequest.builder().email(EMAIL).build();
        resetPasswordRequest = ResetPasswordRequest.builder().password("123").retypePassword("123").build();
    }

    @Test
    void givenEmailValid_whenForgotPassword_thenSendEmailSuccess() throws Exception {
        final String MESSAGE_SEND_EMAIL_SUCCESS = "send email success";

        given(iUserService.sendMail(EMAIL, SITE_URL)).willReturn(MESSAGE_SEND_EMAIL_SUCCESS);

        MvcResult mvcResult = mvc.perform(post("/api/v1/forgot_password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(forgotPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);

        assertEquals(200, status);
        assertEquals(MESSAGE_SEND_EMAIL_SUCCESS, jsonObject.get("resetPasswordToken"));
    }

    @Test
    void givenSendMailThrowsUserNotFoundException_whenForgotPassword_thenSendEmailFail() throws Exception {
        String message = "user not exist";

        given(iUserService.sendMail(EMAIL, SITE_URL)).willThrow(new UserNotFoundException(message));

        MvcResult mvcResult = mvc.perform(post("/api/v1/forgot_password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(forgotPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(message, content);
    }

    @Test
    void givenSendMailThrowsMessagingException_whenForgotPassword_thenSendEmailFail() throws Exception {
        String message = "message exception";

        given(iUserService.sendMail(EMAIL, SITE_URL)).willThrow(new MessagingException(message));

        MvcResult mvcResult = mvc.perform(post("/api/v1/forgot_password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(forgotPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(message, content);
    }

    @Test
    void givenPasswordNotMatch_whenResetPassword_thenResetPasswordFail() throws Exception {
        resetPasswordRequest.setRetypePassword("not same");

        String resetPasswordToken = "6gT1A7vfnbhXuU9PaRfePxEy1NKkE9QSt9Vfw9b7Maiel5mbQ59AMPL";
        String nPassword = resetPasswordRequest.getPassword();
        String messageException = "Password and retype password not match";

        doThrow(new PasswordMismatchException(messageException)).when(forgotPasswordValidator).checkPasswordsMatch(resetPasswordRequest);
        given(iUserService.resetPassword(resetPasswordToken, nPassword)).willReturn("update password success");

        MvcResult mvcResult = mvc.perform(post("/api/v1/reset_password?token=" + resetPasswordToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(resetPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(messageException, content);
    }

    @Test
    void givenPasswordValid_whenResetPassword_thenResetPasswordSuccess() throws Exception {
        String resetPasswordToken = "6gT1A7vfnbhXuU9PaRfePxEy1NKkE9QSt9Vfw9b7Maiel5mbQ59AMPL";
        String nPassword = resetPasswordRequest.getPassword();

        doNothing().when(forgotPasswordValidator).checkPasswordsMatch(resetPasswordRequest);
        given(iUserService.resetPassword(resetPasswordToken, nPassword)).willReturn("update password success");

        MvcResult mvcResult = mvc.perform(post("/api/v1/reset_password?token=" + resetPasswordToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(resetPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals("update password success", content);
    }

    @Test
    void givenThrowsException_whenResetPassword_thenResetPasswordFail() throws Exception {
        String resetPasswordToken = "6gT1A7vfnbhXuU9PaRfePxEy1NKkE9QSt9Vfw9b7Maiel5mbQ59AMPL";
        String nPassword = resetPasswordRequest.getPassword();
        String messageException = "exception";

        doNothing().when(forgotPasswordValidator).checkPasswordsMatch(resetPasswordRequest);
        given(iUserService.resetPassword(resetPasswordToken, nPassword)).willThrow(new RuntimeException(messageException));

        MvcResult mvcResult = mvc.perform(post("/api/v1/reset_password?token=" + resetPasswordToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Utility.mapToJson(resetPasswordRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(500, status);
        assertEquals(messageException, content);
    }
}
