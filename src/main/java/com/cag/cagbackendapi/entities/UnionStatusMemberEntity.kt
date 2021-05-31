package com.cag.cagbackendapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "union_status_member")
data class UnionStatusMemberEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var union_status_member_id: UUID?,

    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_profile"))
    var profileEntity: ProfileEntity?,

    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_union_status"))
    var unionStatusEntity: UnionStatusEntity?
)
