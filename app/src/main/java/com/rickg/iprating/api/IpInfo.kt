package com.rickg.iprating.api

import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val ip: String,
    val bogon: String?,
    val hostname: String?,
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