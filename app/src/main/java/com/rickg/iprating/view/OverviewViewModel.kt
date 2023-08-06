package com.rickg.iprating.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickg.iprating.api.CfTrace
import com.rickg.iprating.api.IpInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class OverviewViewModel : ViewModel() {
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
    val cfTraceLoading: CfTrace = CfTrace("loading", "loading")

    val ipInfo: MutableLiveData<IpInfo> = MutableLiveData()
    val cfTrace: MutableLiveData<CfTrace> = MutableLiveData()

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
        ipInfo.value = httpClient.get("https://ipinfo.io/$ip").body()
    }

    suspend fun fetchCfTrace() {
        cfTrace.value = cfTraceLoading
        val client = HttpClient()
        val responseBody = client.get("https://www.cloudflare.com/cdn-cgi/trace").bodyAsText()

        val warp = responseBody.split("\n")
            .find { it.startsWith("warp=") }
            ?.split("=")
            ?.get(1)

        val colo = responseBody.split("\n")
            .find { it.startsWith("colo=") }
            ?.split("=")
            ?.get(1)

        cfTrace.value = CfTrace(colo, warp)
    }
}