package com.cag.cagbackendapi.entities;

import com.cag.cagbackendapi.dtos.UserDto;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String pass;
    private Boolean active_status = true;
    private String session_id;
    private String img_url;
    private Boolean agreed_18;

    public UserDto toDto() {
        return new UserDto(this.userId, this.first_name, this.last_name, this.email, this.active_status, this.session_id, this.img_url, this.agreed_18);
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setEmailJava(String email) {
        this.email = email;
    }

    public void setActiveStatus(Boolean active_status) { this.active_status = active_status; }
}
