package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "profile_photo")
data class ProfilePhotoEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var profile_photo_id: UUID?,
    var photo_url: String?,

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    var profileEntity: ProfileEntity?
)
