package com.adyanf.clone.instagram.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyInfo(
    @Expose
    @SerializedName("id")
    val id: String,
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
