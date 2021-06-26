package com.cag.cagbackendapi.services.uuid.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.services.uuid.UuidServiceI
import org.springframework.stereotype.Service
import java.util.*

@Service
class UuidService: UuidServiceI {
    override fun generateRandomUUID(): UUID {
        return UUID.randomUUID()
    }

    override fun validateUUID(uuidStr: String?): UUID {
        if (uuidStr.isNullOrEmpty()) {
            throw BadRequestException(DetailedErrorMessages.INVALID_UUID, null)
        }

        return try {
            UUID.fromString(uuidStr)
        } catch (ex: Exception) {
            throw BadRequestException(DetailedErrorMessages.INVALID_UUID, ex)
        }
    }
}