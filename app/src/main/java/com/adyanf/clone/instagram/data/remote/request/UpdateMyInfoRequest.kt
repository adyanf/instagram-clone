package com.adyanf.clone.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateMyInfoRequest(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("profilePicUrl")
    val profilePicUrl: String? = null,
    @Expose
    @SerializedName("tagline")
    val tagline: String? = null
)
