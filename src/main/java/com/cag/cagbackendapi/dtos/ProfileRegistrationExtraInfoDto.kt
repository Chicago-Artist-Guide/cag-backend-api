package com.cag.cagbackendapi.dtos

import com.cag.cagbackendapi.entities.PastPerformanceEntity
import com.cag.cagbackendapi.entities.TrainingEntity
import com.cag.cagbackendapi.entities.UpcomingShowEntity
import com.cag.cagbackendapi.entities.AwardEntity


data class ProfileRegistrationExtraInfoDto(
        var training: List<TrainingEntity>? = null,
        var past_performance: List<PastPerformanceEntity>? = null,
        var upcoming: List<UpcomingShowEntity>? = null,
        var additional_skills_checkboxes: List<String>? = null,
        var additional_skills_manuel: List<String>? = null,
        var awards: List<AwardEntity>? = null,
)
