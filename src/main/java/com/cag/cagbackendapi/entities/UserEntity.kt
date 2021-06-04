package com.cag.cagbackendapi.entities

import com.cag.cagbackendapi.dtos.UserDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
data class UserEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", updatable = false, nullable = false)
    var userId: UUID?,
    var first_name: String?,
    var last_name: String?,
    var email: String?,
    var pass: String?,
    var active_status: Boolean?,
    var session_id: String?,
    var img_url: String?,
    var agreed_18: Boolean?) {
    fun toDto(): UserDto {
        return UserDto(
            this.userId,
            this.first_name,
            this.last_name,
            this.email,
            this.active_status,
            this.session_id,
            this.img_url,
            this.agreed_18);
    }
}
