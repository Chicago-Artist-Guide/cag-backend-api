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
@Table(name = "upcoming_show")
public class UpcomingShowEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID upcoming_show_id;
    private String photo_url;
    private String show_title;
    private String show_synopsis;
    private String industry_code;
    private String show_url;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_profile"))
    private ProfileEntity profileEntity;
}
