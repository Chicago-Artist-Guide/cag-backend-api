package com.cag.cagbackendapi.controller;

import com.cag.cagbackendapi.error.exceptions.UnauthorizedException;
import com.cag.cagbackendapi.model.UserModel;
import com.cag.cagbackendapi.service.user.UserService;
import com.cag.cagbackendapi.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final ValidationService validationService;
    private final UserService userService;

    @Autowired
    UserController(UserService userService, ValidationService validationService) {
        this.validationService = validationService;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserModel> registerUser(
            @RequestBody UserModel userModel,
            @RequestHeader("authKey") String authKey
    ) throws UnauthorizedException {
        this.validationService.validateAuthKey(authKey);
        this.userService.registerUser(userModel);

        return new ResponseEntity<>(userModel, HttpStatus.CREATED);
    }
}
