package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ethnicity_member")
data class EthnicityMemberEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var ethnicity_member_id: UUID?,

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?,

    @ManyToOne
    @JoinColumn(name = "ethnicity_id", referencedColumnName = "ethnicity_id")
    var ethnicityEntity: EthnicityEntity?
)
