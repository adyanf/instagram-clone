package com.adyanf.clone.instagram.utils.network

interface NetworkHelper {

    fun isNetworkConnected(): Boolean

    fun castToNetworkError(throwable: Throwable): NetworkError

}