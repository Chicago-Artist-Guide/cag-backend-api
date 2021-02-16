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
@Table(name = "union_status_member")
public class UnionStatusMemberEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID union_status_member_id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_profile"))
    private ProfileEntity profileEntity;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_union_status"))
    private UnionStatusEntity unionStatusEntity;
}
