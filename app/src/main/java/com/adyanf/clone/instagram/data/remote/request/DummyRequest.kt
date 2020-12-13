package com.adyanf.clone.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DummyRequest(
    @Expose
    @SerializedName("id")
    var id: String
)