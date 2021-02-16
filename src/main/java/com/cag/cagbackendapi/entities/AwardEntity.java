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
@Table(name = "award")
public class AwardEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID award_id;
    private String name;
    private String year_received;
    private String award_url;
    private String description;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_profile"))
    private ProfileEntity profileEntity;
}
