package com.cag.cagbackendapi.dtos

import com.cag.cagbackendapi.entities.AwardEntity
import com.cag.cagbackendapi.entities.PastPerformanceEntity
import com.cag.cagbackendapi.entities.TrainingEntity
import com.cag.cagbackendapi.entities.UpcomingShowEntity
import java.util.*

data class ProfileExtraInfoDto(
        var profile_extra_info: UUID?,
        var profile_id: UUID?,
        var training: TrainingEntity?,
        var past_performance: PastPerformanceEntity,
        var upcoming_show: UpcomingShowEntity,
        var additional_skills_checkbox: String?,
        var additional_skills_manuel: String?,
        var awards: AwardEntity?,
        var userEntity: UserDto? )