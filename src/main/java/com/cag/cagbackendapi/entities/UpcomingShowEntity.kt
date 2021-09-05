package com.cag.cagbackendapi.entities;

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "upcoming_show")
data class UpcomingShowEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var upcoming_show_id: UUID?,
    var photo_url: String?,
    var show_title: String?,
    var show_synopsis: String?,
    var industry_code: String?,
    var show_url: String?,

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?
)
