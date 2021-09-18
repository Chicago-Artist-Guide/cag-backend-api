package com.cag.cagbackendapi.dtos

import java.lang.reflect.Array
import java.util.*

data class ProfileExtraInfo(
    var profile_id: ProfileDto?,

    var training_id: UUID?,
    var institution: String?,
    var degree: String?,
    var start_year: Date?,
    var end_year: Date?,
    var country: String?,
    var city: String?,
    var state: String?,
    var notes: List<String>,

    var past_performance_id: UUID?,
    var past_show_title: String?,
    var role: String?,
    var theater_or_location: String?,
    var past_show_url: String?,
    var director: String?,
    var musical_director: String?,
    var theater_group: String?,

    var upcoming_show_id: UUID?,
    var photo_url: String?,
    var upcoming_show_title: String?,
    var show_synopsis: String?,
    var industry_code: String?,
    var upcoming_show_url: String?,

    var award_id: UUID?,
    var name: String?,
    var year_received: Int?,
    var award_url: String?,
    var description: String?
)