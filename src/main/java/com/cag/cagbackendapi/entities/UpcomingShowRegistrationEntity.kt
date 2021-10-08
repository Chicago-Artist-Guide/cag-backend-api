package com.cag.cagbackendapi.entities;

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

class UpcomingShowRegistrationEntity (

    var photo_url: String?,
    var show_title: String?,
    var show_synopsis: String?,
    var industry_code: String?,
    var show_url: String?,
)
