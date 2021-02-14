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
@Table(name = "profile_photo")
public class ProfilePhotoEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID profile_photo_id;
    private String photo_url;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_profile"))
    private ProfileEntity profileEntity;
}
