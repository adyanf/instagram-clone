package com.adyanf.clone.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostLikeModifyRequest(
    @Expose
    @SerializedName("postId")
    val postId: String
)