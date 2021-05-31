package com.cag.cagbackendapi.entities;

import com.cag.cagbackendapi.dtos.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profile")
data class ProfileEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var profile_id: UUID?,
    var pronouns: String?,
    var lgbtqplus_member: Boolean?,
    var gender_identity: String?,
    var comfortable_playing_man: Boolean?,
    var comfortable_playing_women: Boolean?,
    var comfortable_playing_neither: Boolean?,
    var comfortable_playing_transition: Boolean?,
    var height_inches: Int?,
    var agency: String?,
    var website_type_one: String?,
    var website_link_one: String?,
    var website_type_two: String?,
    var website_link_two: String?,
    var bio: String?,

    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    var userEntity: UserEntity?) {
    fun toDto(): ProfileDto {
        return ProfileDto(
            this.profile_id,
            this.pronouns,
            this.lgbtqplus_member,
            this.gender_identity,
            this.comfortable_playing_man,
            this.comfortable_playing_women,
            this.comfortable_playing_neither,
            this.comfortable_playing_transition,
            this.height_inches,
            this.agency,
            this.website_link_one,
            this.website_link_two,
            this.website_type_one,
            this.website_type_two,
            this.bio,
            this.userEntity?.toDto())
    }
}
