package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "skill_member")
data class SkillMemberEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var skill_member_id: UUID?,

    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_profile"))
    var profileEntity: ProfileEntity?,

    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_skill"))
    var skillEntity: SkillEntity?
)
