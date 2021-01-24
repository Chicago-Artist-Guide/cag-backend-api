package com.cag.cagbackendapi.entities;

import com.cag.cagbackendapi.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends UuidEntity {
    private UUID id;
    private String name;
    private String email;

    public UserDto toDto() {
        return new UserDto(this.id, this.name, this.email);
    }
}
