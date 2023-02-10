package com.example.ecom.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.ecom.di.getKoinInstance
import com.example.ecom.models.base.ApiState
import com.example.ecom.models.base.BaseResponse
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun <ResultType> makeCall(
    code: Int = 0,
    fetch: suspend () -> BaseResponse<ResultType>
) = flow {
    if (getKoinInstance<Boolean>("IsNetworkConnected").not()) {
        emit(ApiState.NoNetwork)
        return@flow
    }
    val flow = flow {
        try {
            val response = fetch()
            if (response.status && response.isResultAvailable()) {
                emit(ApiState.Success(response, code))
            } else {
                emit(ApiState.Failed(response, code))
            }
        } catch (e: Exception) {
            emit(ApiState.Error(e))
        }
    }
    emitAll(flow)
}

fun isNetworkConnected(context: Context): Boolean {
    val result: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}