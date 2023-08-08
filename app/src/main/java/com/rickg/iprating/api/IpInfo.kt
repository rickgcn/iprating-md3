package com.rickg.iprating.api

import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val ip: String,
    @Serializable(with = BooleanAsStringSerializer::class)
    val bogon: String?,
    val hostname: String?,
    @Serializable(with = BooleanAsStringSerializer::class)
    val anycast: String?,
    val city: String?,
    val region: String?,
    val country: String?,
    val loc: String?,
    val org: String?,
    val postal: String?,
    val timezone: String?,
    val readme: String?
)