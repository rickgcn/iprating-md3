package com.rickg.iprating.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickg.iprating.api.IpInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class QueryViewModel: ViewModel() {
    val ipInfoLoading: IpInfo = IpInfo(
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading",
        "loading"
    )

    val ipInfo: MutableLiveData<IpInfo> = MutableLiveData()

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchIpInfo(ip: String) {
        ipInfo.value = ipInfoLoading
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                    explicitNulls = false
                })
            }
        }
        try {
            ipInfo.value = httpClient.get("https://ipinfo.io/$ip").body()
        } catch (e: Exception) {
            ipInfo.value = IpInfo(
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error",
                "error"
            )
        }
    }
}