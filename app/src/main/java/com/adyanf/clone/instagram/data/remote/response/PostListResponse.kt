package com.adyanf.clone.instagram.data.remote.response

import com.adyanf.clone.instagram.data.model.Post
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostListResponse(
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,
    @Expose
    @SerializedName("status")
    var status: Int,
    @Expose
    @SerializedName("message")
    var message: String,
    @Expose
    @SerializedName("data")
    var data: List<Post>
)
