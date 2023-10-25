package com.hang.stackask.controller;

import com.hang.stackask.data.ReplyAnswerData;
import com.hang.stackask.exception.NotLoggedInException;
import com.hang.stackask.request.ReplyAnswerRequest;
import com.hang.stackask.response.ReplyAnswerResponse;
import com.hang.stackask.service.interfaces.IReplyAnswerService;
import com.hang.stackask.utils.JwtFilterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class ReplyAnswerController {
    @Autowired
    private IReplyAnswerService iReplyAnswerService;

    @Autowired
    private JwtFilterUtil jwtFilterUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static final String NOT_LOGGED_IN = "not logged in";

    @PostMapping("/{id_answer}/reply_answers/create")
    public ResponseEntity<ReplyAnswerResponse> create(
            @PathVariable("id_answer") Long answerId,
            @RequestBody ReplyAnswerRequest replyAnswerRequest,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        ReplyAnswerData replyAnswerData = iReplyAnswerService.create(userId, answerId, replyAnswerRequest);
        ReplyAnswerResponse replyAnswerResponse = modelMapper.map(replyAnswerData, ReplyAnswerResponse.class);

        return new ResponseEntity<>(replyAnswerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/reply_answers/{id}/update")
    public ResponseEntity<ReplyAnswerResponse> update(
            @PathVariable("id") Long replyAnswerId,
            @RequestBody ReplyAnswerRequest replyAnswerRequest,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        ReplyAnswerData replyAnswerData = iReplyAnswerService.update(userId, replyAnswerId, replyAnswerRequest);
        ReplyAnswerResponse replyAnswerResponse = modelMapper.map(replyAnswerData, ReplyAnswerResponse.class);

        return new ResponseEntity<>(replyAnswerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/reply_answers/{id}")
    public ResponseEntity<ReplyAnswerResponse> delete(
            @PathVariable("id") Long replyAnswerId,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        ReplyAnswerData replyAnswerData = iReplyAnswerService.delete(userId, replyAnswerId);
        ReplyAnswerResponse replyAnswerResponse = modelMapper.map(replyAnswerData, ReplyAnswerResponse.class);

        return new ResponseEntity<>(replyAnswerResponse, HttpStatus.OK);
    }
}
