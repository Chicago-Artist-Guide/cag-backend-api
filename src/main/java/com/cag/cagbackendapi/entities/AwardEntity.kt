package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "award")
data class AwardEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    var award_id: UUID?,
    var name: String?,
    var year_received: String?,
    var award_url: String?,
    var description: String?,

    @OneToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_profile"))
    var profileEntity: ProfileEntity?
)