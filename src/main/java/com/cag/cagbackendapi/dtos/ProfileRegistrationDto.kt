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
        var bio: String?,

        var landing_perform_type_on_stage: Boolean? = null,
        var landing_perform_type_off_stage: Boolean? = null,
        var actor_info_1_ethnicities: List<String>? = null,
        var actor_info_2_age_ranges: List<Int>? = null,
        var actor_info_2_gender_roles: List<String>? = null,
        var off_stage_roles_general: List<String>? = null,
        var off_stage_roles_production: List<String>? = null,
        var off_stage_roles_scenic: List<String>? = null,
        var off_stage_roles_lighting: List<String>? = null,
        var off_stage_roles_sound: List<String>? = null,
        var off_stage_roles_hair_makeup_costumes: List<String>? = null,
        var actor_skills: List<String>? = null,
        var actor_ethnicity: List<String>? = null,
        var ethnicity_skills: List<String>? = null,

        var profile_photo_url: String? = null,
        var demographic_union_status: String? = null,
        var demographic_websites: List<String>? = null
)
