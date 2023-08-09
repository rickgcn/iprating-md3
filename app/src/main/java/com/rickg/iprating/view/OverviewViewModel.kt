package com.rickg.iprating.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickg.iprating.api.CfTrace
import com.rickg.iprating.api.IpInfoState
import io.ipinfo.api.IPinfo
import io.ipinfo.api.errors.ErrorResponseException
import io.ipinfo.api.errors.RateLimitedException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {
    val ipInfo: MutableLiveData<IpInfoState> = MutableLiveData()
    val cfTrace: MutableLiveData<CfTrace> = MutableLiveData()
    private val cfTraceLoading: CfTrace = CfTrace("loading", "loading")

    fun fetchIpInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            ipInfo.postValue(IpInfoState.Loading)
            try {
                val client = HttpClient()
                val deviceIP: String = client.request("https://ifconfig.me/ip").body()
                val ipInfoObj = IPinfo.Builder().build()
                val response = ipInfoObj.lookupIP(deviceIP)
                ipInfo.postValue(IpInfoState.Success(response))
            } catch (e: ErrorResponseException) {
                // Handle Error Response
                ipInfo.postValue(IpInfoState.Error(e.message ?: "Unknown Error"))
            } catch (e: RateLimitedException) {
                // Handle Rate Limited
                ipInfo.postValue(IpInfoState.Error(e.message ?: "Unknown Error"))
            } catch (e: Exception) {
                // Handle all other exceptions
                ipInfo.postValue(IpInfoState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun fetchCfTrace() {
        cfTrace.value = cfTraceLoading
        val client = HttpClient()

        try {
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
        } catch (e: Exception) {
            cfTrace.value = CfTrace(null, null)
        }
    }
}