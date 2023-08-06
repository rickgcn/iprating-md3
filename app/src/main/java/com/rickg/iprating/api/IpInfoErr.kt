package com.rickg.iprating.api

import kotlinx.serialization.Serializable

@Serializable
data class IpInfoErr(
    val status: Int,
    val error: IpInfoErrMsg
) {
    @Serializable
    data class IpInfoErrMsg(
        val title: String,
        val message: String
    )
}