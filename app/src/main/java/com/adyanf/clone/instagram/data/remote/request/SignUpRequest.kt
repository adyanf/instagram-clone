package com.adyanf.clone.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("email")
    var email: String,
    @Expose
    @SerializedName("password")
    var password: String
)
