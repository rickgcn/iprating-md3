package com.rickg.iprating.api

import io.ipinfo.api.model.IPResponse

sealed class IpInfoState {
    object Loading : IpInfoState()
    data class Success(val ipInfo: IPResponse) : IpInfoState()
    data class Error(val message: String) : IpInfoState()
}