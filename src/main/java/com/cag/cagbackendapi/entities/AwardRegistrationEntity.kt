package com.cag.cagbackendapi.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

class AwardRegistrationEntity (
    var name: String?,
    var year_received: Int?,
    var award_url: String?,
    var description: String?,

)