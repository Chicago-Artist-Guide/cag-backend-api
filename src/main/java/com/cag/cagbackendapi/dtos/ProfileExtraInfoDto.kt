package com.cag.cagbackendapi.dtos

import com.cag.cagbackendapi.entities.*
import java.util.*

data class ProfileExtraInfoDto(
        var profile_id: UUID?,
        var training: List<TrainingEntity>? = null,
        var past_performance: List<PastPerformanceEntity>? = null,
        var upcoming: List<UpcomingShowEntity>? = null,
        var additional_skills_checkboxes: List<String>? = null,
        var additional_skills_manual: List<String>? = null,
        var awards: List<AwardEntity>? = null,
        var userEntity: UserDto?
        )