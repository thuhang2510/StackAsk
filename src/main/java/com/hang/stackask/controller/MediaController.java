package com.hang.stackask.controller;

import com.hang.stackask.data.MediaData;
import com.hang.stackask.exception.NotLoggedInException;
import com.hang.stackask.response.MediaResponse;
import com.hang.stackask.service.interfaces.IMediaService;
import com.hang.stackask.utils.JwtFilterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {
    @Autowired
    private IMediaService iMediaService;

    @Autowired
    private JwtFilterUtil jwtFilterUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static final String NOT_LOGGED_IN = "not logged in";

    @PostMapping(value = "{parent_uuid}/add")
    public ResponseEntity<MediaResponse> create(@RequestParam("image") MultipartFile multipartFile
            , @PathVariable("parent_uuid") String parentUuid, @RequestHeader("Authorization") String authHeader) throws IOException {
        Long userId = jwtFilterUtil.getDataFromJwt(authHeader);

        if(userId == null)
            throw new NotLoggedInException(NOT_LOGGED_IN);

        MediaData mediaData = iMediaService.create(userId, parentUuid, multipartFile);
        MediaResponse mediaResponse = modelMapper.map(mediaData, MediaResponse.class);

        return new ResponseEntity<>(mediaResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable("uuid") String uuid){
        iMediaService.delete(uuid);

        return new ResponseEntity<>("delete success", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{uuid}/details")
    public ResponseEntity<MediaResponse> get(@PathVariable("uuid") String uuid) throws IOException {
        MediaData mediaData = iMediaService.get(uuid);
        MediaResponse mediaResponse = modelMapper.map(mediaData, MediaResponse.class);
        return new ResponseEntity<>(mediaResponse, HttpStatus.OK);
    }
}
