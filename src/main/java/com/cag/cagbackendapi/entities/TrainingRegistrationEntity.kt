package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

class TrainingRegistrationEntity (

    var institution: String?,
    var degree: String?,
    var start_year: Short?,
    var end_year: Short?,
    var country: String?,
    var city: String?,
    var state: String?,
    var notes: String?,

)
