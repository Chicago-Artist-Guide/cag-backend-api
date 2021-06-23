package com.cag.cagbackendapi.utilities.impl;

import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.utilities.ProfileUtilitiesI;

import java.util.Random;

public class ProfileUtilities implements ProfileUtilitiesI {

    public static String randomEmail() {
        String randomChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * randomChars.length());
            salt.append(randomChars.charAt(index));
        }
        salt.append("@gmail.com");
        String saltStr = salt.toString();
        return saltStr;
    }

    public static UserRegistrationDto validRegisterUser(){
        String randomUserEmail = randomEmail();
        UserRegistrationDto validRegistrationDto = new UserRegistrationDto("first name", "last name", randomUserEmail, "password", true, true);

        return validRegistrationDto;
    }

}
