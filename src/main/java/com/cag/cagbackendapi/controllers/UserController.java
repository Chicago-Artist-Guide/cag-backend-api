package com.cag.cagbackendapi.controllers;

import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.dtos.UserDto;
import com.cag.cagbackendapi.dtos.UserUpdateDto;
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
    public ResponseEntity<UserDto> registerUser(
            @RequestHeader("authKey") String authKey,
            @RequestBody UserRegistrationDto userRegistrationDto
    ) {
        this.validationService.validateAuthKey(authKey);
        UserDto userResponseDto = this.userService.registerUser(userRegistrationDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> updateUser(
            @RequestHeader("authKey") String authKey,
            @RequestBody UserUpdateDto userRequestDto,
            @PathVariable("userId") String userId
    ){
        this.validationService.validateAuthKey(authKey);
        UserDto userResponseDto = this.userService.updateUser(userId, userRequestDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> deleteUser(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId

    ) {
        this.validationService.validateAuthKey(authKey);
        UserDto userResponseDto = this.userService.deleteUser(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> getByUserId(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId
    ){
        this.validationService.validateAuthKey(authKey);
        UserDto userResponseDto = this.userService.getByUserId(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
