package com.hang.stackask.controller;

import com.hang.stackask.data.AnswerData;
import com.hang.stackask.exception.NotLoggedInException;
import com.hang.stackask.request.AnswerRequest;
import com.hang.stackask.response.AnswerResponse;
import com.hang.stackask.service.interfaces.IAnswerService;
import com.hang.stackask.utils.JwtFilterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class AnswerController {
    @Autowired
    private IAnswerService iAnswerService;

    @Autowired
    private JwtFilterUtil jwtFilterUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static final String NOT_LOGGED_IN = "not logged in";

    @PostMapping("/{uuid_question}/answers/create")
    public ResponseEntity<AnswerResponse> create(
            @PathVariable("uuid_question") String uuidQuestion,
            @RequestBody AnswerRequest answerRequest,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        AnswerData answerData = iAnswerService.create(uuidQuestion, userId, answerRequest);
        AnswerResponse answerResponse = modelMapper.map(answerData, AnswerResponse.class);

        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/answers/{id}/update")
    public ResponseEntity<AnswerResponse> create(
            @PathVariable("id") Long idAnswer,
            @RequestBody AnswerRequest answerRequest,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        AnswerData answerData = iAnswerService.update(idAnswer, userId, answerRequest);
        AnswerResponse answerResponse = modelMapper.map(answerData, AnswerResponse.class);

        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/answers/{id}/delete")
    public ResponseEntity<AnswerResponse> delete(
            @PathVariable("id") Long idAnswer,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        AnswerData answerData = iAnswerService.delete(idAnswer, userId);
        AnswerResponse answerResponse = modelMapper.map(answerData, AnswerResponse.class);

        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<AnswerResponse> getById(@PathVariable("id") Long idAnswer){
        AnswerData answerData = iAnswerService.getById(idAnswer);
        AnswerResponse answerResponse = modelMapper.map(answerData, AnswerResponse.class);

        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }
}
