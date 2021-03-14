package com.cag.cagbackendapi.entities;

import com.cag.cagbackendapi.dtos.UserDto;
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
    public UUID userId;
    public String first_name;
    public String last_name;
    public String email;
    public Boolean active_status;
    public String session_id;
    public String img_url;
    public Boolean agreed_18;

    public UserDto toDto() {
        return new UserDto(this.userId, this.first_name, this.last_name, this.email, this.active_status, this.session_id, this.img_url, this.agreed_18);
    }
}
