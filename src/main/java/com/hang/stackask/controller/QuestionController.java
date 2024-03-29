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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

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
            @RequestHeader("Authorization") String authHeader){

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

    @GetMapping("/{uuid}")
    public ResponseEntity<QuestionResponse> getByUuid(@PathVariable("uuid") String uuid){
        QuestionData questionData = iQuestionService.getByUuid(uuid);
        QuestionResponse questionResponse = modelMapper.map(questionData, QuestionResponse.class);

        return new ResponseEntity<>(questionResponse, HttpStatus.OK);
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

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(
            @PathVariable("id") Long questionId,
            @RequestBody QuestionRequest questionRequest,
            @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        QuestionData questionData = iQuestionService.update(questionId, questionRequest);
        QuestionResponse questionResponse = modelMapper.map(questionData, QuestionResponse.class);

        return new ResponseEntity<>(questionResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<QuestionResponse> updateView(@PathVariable("id") Long questionId,
                                                       @RequestBody QuestionRequest questionRequest,
                                                       @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        QuestionData questionData = iQuestionService.updateView(questionId, questionRequest);
        QuestionResponse questionResponse = modelMapper.map(questionData, QuestionResponse.class);

        return new ResponseEntity<>(questionResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/vote-up")
    public ResponseEntity<QuestionResponse> updateVote(@PathVariable("id") Long questionId,
                                                       @RequestBody QuestionRequest questionRequest,
                                                       @RequestHeader("Authorization") String authHeader){

        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if (userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        QuestionData questionData = iQuestionService.updateVote(questionId, questionRequest);
        QuestionResponse questionResponse = modelMapper.map(questionData, QuestionResponse.class);

        return new ResponseEntity<>(questionResponse, HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<PageResponse<List<QuestionResponse>>> getByTags(
            @RequestParam("limit") int limit,
            @RequestParam("tags") Set<String> tagsName,
            @RequestParam("next_page_token") String nextPageToken) throws UnsupportedEncodingException {

        List<QuestionData> questionsData = iQuestionService.processGetByTagsInAndCursorAndPageable(tagsName, nextPageToken, limit);
        PageResponse<List<QuestionResponse>> pageResponse = ListToPaginationConverter.toPagination(questionsData, limit);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }
}
