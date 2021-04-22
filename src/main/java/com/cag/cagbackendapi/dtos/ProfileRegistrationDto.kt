package com.cag.cagbackendapi.dtos

data class ProfileRegistrationDto(
        var pronouns: String?,
        var lgbtqplus_member: Boolean?,
        var gender_identity: String?,
        var comfortable_playing_man: Boolean?,
        var comfortable_playing_women: Boolean?,
        var comfortable_playing_neither: Boolean?,
        var comfortable_playing_transition: Boolean?,
        var height_inches: Int?,
        var agency: String?,
        var website_link_one: String?,
        var website_link_two: String?,
        var website_type_one: String?,
        var website_type_two: String?,
        var bio: String?
)
