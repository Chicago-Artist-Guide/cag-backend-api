package com.cag.cagbackendapi.dtos

import com.cag.cagbackendapi.entities.*


data class ProfileRegistrationExtraInfoDto(
        //var training: List<TrainingEntity>? = null,
        var training: List<TrainingRegistrationEntity>? = null,
        //var past_performance: List<PastPerformanceEntity>? = null,
        var past_performance: List<PastPerformanceRegistrationEntity>? = null,
        //var upcoming: List<UpcomingShowEntity>? = null,
        var upcoming: List<UpcomingShowRegistrationEntity>? = null,
        var additional_skills_checkboxes: List<String>? = null,
        var additional_skills_manual: List<String>? = null,
        //var awards: List<AwardEntity>? = null,
        var awards1: List<AwardRegistrationEntity>? = null
)
