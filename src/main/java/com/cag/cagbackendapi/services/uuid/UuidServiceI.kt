package com.cag.cagbackendapi.services.uuid

import java.util.*

interface UuidServiceI {
    fun generateRandomUUID(): UUID
    fun validateUUID(uuidStr: String?): UUID
}