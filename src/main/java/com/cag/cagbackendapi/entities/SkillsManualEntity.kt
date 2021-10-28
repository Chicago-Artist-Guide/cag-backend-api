package com.cag.cagbackendapi.entities

import lombok.Data
import org.hibernate.annotations.GenericGenerator
//import org.hibernate.annotations.Table
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.*;


@Entity
//@Table(name = "add_skills_manual")
@Table(name = "add_skills_manual")
data class SkillsManualEntity(
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    var skills_id: UUID?,

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?,

    var skills: String?

)
