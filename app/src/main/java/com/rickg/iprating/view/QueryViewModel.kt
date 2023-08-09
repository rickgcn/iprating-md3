package com.rickg.iprating.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickg.iprating.api.IpInfoState
import io.ipinfo.api.IPinfo
import io.ipinfo.api.errors.ErrorResponseException
import io.ipinfo.api.errors.RateLimitedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QueryViewModel : ViewModel() {
    val ipInfo: MutableLiveData<IpInfoState> = MutableLiveData()

    fun fetchIpInfo(ip: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ipInfo.postValue(IpInfoState.Loading)
            try {
                val ipInfoObj = IPinfo.Builder().build()
                val response = ipInfoObj.lookupIP(ip)
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
}