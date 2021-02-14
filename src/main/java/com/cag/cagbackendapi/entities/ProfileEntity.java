package com.cag.cagbackendapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private UUID profile_id;
    private String pronouns;
    private Boolean lgbtqplus_member;
    private String gender_identity;
    private Boolean comfortable_playing_man;
    private Boolean comfortable_playing_women;
    private Boolean comfortable_playing_neither;
    private Boolean comfortable_playing_transition;
    private int height_feet;
    private int height_inches;
    private String agency;
    private String website_type_one;
    private String website_link_one;
    private String website_type_two;
    private String website_link_two;
    private String bio;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user"))
    private UserEntity userEntity;
}
