package com.cag.cagbackendapi.dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String name;
    private String email;

    public String toString() {
        return "name: " + this.name + ", email: " + this.email;
    }
}
