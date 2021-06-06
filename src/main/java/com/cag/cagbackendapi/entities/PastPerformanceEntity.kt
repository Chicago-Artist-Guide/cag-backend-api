package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "past_performance")
data class PastPerformanceEntity (
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var past_performance_id: UUID?,
    var show_title: String?,
    var role: String?,
    var theater_or_location: String?,
    var show_url: String?,
    var director: String?,
    var musical_director: String?,
    var theater_group: String?,

    @OneToOne
    @JoinColumn(foreignKey =ForeignKey(name = "fk_profile"))
    var profileEntity: ProfileEntity?
)
