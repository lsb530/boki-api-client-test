package com.boki.bokiapiclienttest.dto

import io.jsonwebtoken.Claims
import kotlin.reflect.full.memberProperties

data class UserInfo(
    val name: String? = "tester",
    val age: Int? = 20,
)

fun UserInfo.asMap() = this::class.memberProperties.associate { it.name to it.getter.call(this) }

fun Claims.toUserInfo(): UserInfo {
    return UserInfo(
        name = this["name"] as String,
        age = this["age"] as Int,
    )
}