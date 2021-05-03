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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID profile_id;
    private String pronouns;
    private Boolean lgbtqplus_member;
    private String gender_identity;
    private Boolean comfortable_playing_man;
    private Boolean comfortable_playing_women;
    private Boolean comfortable_playing_neither;
    private Boolean comfortable_playing_transition;
    private int height_inches;
    private String agency;
    private String website_type_one;
    private String website_link_one;
    private String website_type_two;
    private String website_link_two;
    private String bio;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    public ProfileDto toDto(){
        return new ProfileDto(this.profile_id, this.pronouns, this.lgbtqplus_member, this.gender_identity, this.comfortable_playing_man,this.comfortable_playing_women, this.comfortable_playing_neither,this.comfortable_playing_transition, this.height_inches, this.agency, this.website_link_one, this.website_link_two, this.website_type_one, this.website_type_two, this.bio, this.userEntity);
    }
}
