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
@Table(name = "ethnicity_member")
public class EthnicityMemberEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID ethnicity_member_id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_profile"))
    private ProfileEntity profileEntity;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ethnicity"))
    private EthnicityEntity ethnicityEntity;
}
