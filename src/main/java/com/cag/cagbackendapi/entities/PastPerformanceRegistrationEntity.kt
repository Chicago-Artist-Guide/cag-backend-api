package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

class PastPerformanceRegistrationEntity (

    var show_title: String?,
    var role: String?,
    var theater_or_location: String?,
    var show_url: String?,
    var director: String?,
    var musical_director: String?,
    var theater_group: String?,

)
