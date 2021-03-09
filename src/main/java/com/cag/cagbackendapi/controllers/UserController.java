package com.cag.cagbackendapi.controllers;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto;
import com.cag.cagbackendapi.dtos.UserDto;
import com.cag.cagbackendapi.dtos.UserResponseDto;
import com.cag.cagbackendapi.services.user.impl.UserService;
import com.cag.cagbackendapi.services.validation.impl.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

    private final ValidationService validationService;
    private final UserService userService;

    @Autowired
    UserController(UserService userService, ValidationService validationService) {
        this.validationService = validationService;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestHeader("authKey") String authKey,
            @RequestBody RegisterUserRequestDto registerUserRequestDto
    ) {
        this.validationService.validateAuthKey(authKey);
        UserResponseDto userResponseDto = this.userService.registerUser(registerUserRequestDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestHeader("authKey") String authKey,
            @RequestBody UserDto userRequestDto
    ){
        this.validationService.validateAuthKey(authKey);
        UserResponseDto userResponseDto = this.userService.updateUser(userRequestDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> deleteUser(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId

    ) {
        this.validationService.validateAuthKey(authKey);
        UserResponseDto userResponseDto = this.userService.deleteUser(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> getByUserId(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId
    ){
        this.validationService.validateAuthKey(authKey);
        UserResponseDto userResponseDto = this.userService.getByUserId(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
