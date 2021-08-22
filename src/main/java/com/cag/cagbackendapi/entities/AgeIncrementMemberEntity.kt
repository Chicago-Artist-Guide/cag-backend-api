package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "age_increment_member")
data class AgeIncrementMemberEntity(
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var age_increment_member_id: UUID?,

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?,

    @ManyToOne
    @JoinColumn(name = "age_increment_id", referencedColumnName = "age_increment_id")
    var ageIncrementEntity: AgeIncrementEntity?
)

