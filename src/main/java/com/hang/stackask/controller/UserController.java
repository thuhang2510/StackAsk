package com.hang.stackask.controller;

import com.hang.stackask.data.UserData;
import com.hang.stackask.response.UserResponse;
import com.hang.stackask.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hang.stackask.utils.MapperUtil.mapList;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAll(){
        List<UserData> userDatas = iUserService.getAll();
        List<UserResponse> userResponses = mapList(userDatas, UserResponse.class);

        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponse> get(@PathVariable(value = "uuid") String uuid){
        UserData userData = iUserService.getByUuid(uuid);
        UserResponse userResponse = modelMapper.map(userData, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
