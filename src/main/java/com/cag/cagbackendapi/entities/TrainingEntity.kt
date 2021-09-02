package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "training")
data class TrainingEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var training_id: UUID?,
    var institution: String?,
    var degree: String?,
    var start_year: Short?,
    var end_year: Short?,
    var country: String?,
    var city: String?,
    var state: String?,
    var notes: String?,

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?
)
