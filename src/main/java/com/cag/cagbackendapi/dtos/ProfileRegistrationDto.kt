package com.cag.cagbackendapi.dtos

data class ProfileRegistrationDto(
        var pronouns: String? = null,
        var lgbtqplus_member: Boolean? = null,
        var gender_identity: String? = null,
        var comfortable_playing_man: Boolean? = null,
        var comfortable_playing_women: Boolean? = null,
        var comfortable_playing_neither: Boolean? = null,
        var comfortable_playing_transition: Boolean? = null,
        var height_inches: Int? = null,
        var agency: String? = null,
        var website_link_one: String? = null,
        var website_link_two: String? = null,
        var website_type_one: String? = null,
        var website_type_two: String? = null,
        var bio: String? = null
)
