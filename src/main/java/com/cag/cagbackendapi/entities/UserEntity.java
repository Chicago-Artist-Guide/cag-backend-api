package com.cag.cagbackendapi.entities;

import com.cag.cagbackendapi.dtos.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;
    private String first_name;
    private String last_name;
    private String email;
    private Boolean active_status;
    private String session_id;

    public UserResponseDto toDto() {
        return new UserResponseDto(this.userId, this.first_name, this.last_name, this.email, this.active_status, this.session_id);
    }
}
