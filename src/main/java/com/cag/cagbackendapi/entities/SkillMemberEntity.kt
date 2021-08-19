package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "skill_member")
data class SkillMemberEntity(
        @Id
        @Type(type = "pg-uuid")
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator"
        )
        var skill_member_id: UUID?,

        @ManyToOne
        @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
        var profileEntity: ProfileEntity?,

        @ManyToOne
        @JoinColumn(name = "skill_id", referencedColumnName = "skill_id")
        var skillEntity: SkillEntity?
)