package com.adyanf.clone.instagram.data.repository

import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.remote.NetworkService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val networkService: NetworkService) {

    suspend fun uploadPhoto(file: File, user: User): String {
        return MultipartBody.Part.createFormData(
            "image", file.name, RequestBody.create(MediaType.parse("image/*"), file)
        ).run {
            return@run networkService.doImageUploadCall(this, user.id, user.accessToken).data.imageUrl
        }
    }
}