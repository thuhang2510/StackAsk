package com.hang.stackask.controller;

import com.hang.stackask.converter.ListToPaginationConverter;
import com.hang.stackask.response.PageResponse;
import com.hang.stackask.data.QuestionData;
import com.hang.stackask.exception.NotLoggedInException;
import com.hang.stackask.request.QuestionRequest;
import com.hang.stackask.response.QuestionResponse;
import com.hang.stackask.service.interfaces.IQuestionService;
import com.hang.stackask.utils.JwtFilterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    private JwtFilterUtil jwtFilterUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static final String NOT_LOGGED_IN = "not logged in";

    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> create(
            @RequestBody QuestionRequest questionRequest,
            @RequestHeader("Authorization") String authHeader) throws IOException {

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        QuestionData questionData = iQuestionService.create(userId, questionRequest);
        QuestionResponse questionResponse = modelMapper.map(questionData, QuestionResponse.class);

        return new ResponseEntity<>(questionResponse, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<List<QuestionResponse>>> getQuestionsByCursor(
            @RequestParam("limit") int limit,
            @RequestParam("next_page_token") String nextPageToken) throws UnsupportedEncodingException {

        final List<QuestionData> questionsData = iQuestionService.getQuestionsWithPagination(nextPageToken, limit);
        PageResponse<List<QuestionResponse>> pageResponse = ListToPaginationConverter.toPagination(questionsData, limit);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<List<QuestionResponse>>> getByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam("limit") int limit,
            @RequestParam("next_page_token") String nextPageToken) throws UnsupportedEncodingException {

        List<QuestionData> questionsData = iQuestionService.getByTitleOrContentWithPagination(keyword, nextPageToken, limit);
        PageResponse<List<QuestionResponse>> questionResponses = ListToPaginationConverter.toPagination(questionsData, limit);

        return new ResponseEntity<>(questionResponses, HttpStatus.OK);
    }
}
