package com.adyanf.clone.instagram.utils.network

import android.content.Context

class FakeNetworkHelperImpl constructor(private val context: Context) : NetworkHelper {

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun castToNetworkError(throwable: Throwable): NetworkError {
        return NetworkHelperImpl(context).castToNetworkError(throwable)
    }

}