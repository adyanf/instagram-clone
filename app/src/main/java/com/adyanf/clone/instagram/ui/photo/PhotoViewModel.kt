package com.adyanf.clone.instagram.ui.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.repository.PhotoRepository
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.common.FileUtils
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class PhotoViewModel(
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val postRepository: PostRepository,
    private val directory: File,
) : BaseViewModel(networkHelper) {

    private val user: User = userRepository.getCurrentUser()!!

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()

    override fun onCreate() {}

    fun onGalleryImageSelected(inputStream: InputStream) {
        viewModelScope.launch {
            loading.postValue(true)
            try {
                val savedFile = FileUtils.saveInputStreamToFile(inputStream, directory, "gallery_img_temp", 500)
                if (savedFile != null) {
                    FileUtils.getImageSize(savedFile)?.run {
                        uploadPhotoAndCreatePost(savedFile, this)
                    }
                } else {
                    loading.postValue(false)
                    messageStringId.postValue(Resource.error(R.string.try_again))
                }
            } catch (e: Exception) {
                loading.postValue(false)
                messageStringId.postValue(Resource.error(R.string.try_again))
            }

        }
    }

    fun onCameraImageTaken(cameraImageProcessor: () -> String) {
        viewModelScope.launch {
            loading.postValue(true)
            try {
                val imagePath = cameraImageProcessor()
                File(imagePath).apply {
                    FileUtils.getImageSize(this)?.let { size ->
                        uploadPhotoAndCreatePost(this, size)
                    } ?: loading.postValue(false)
                }
            } catch (e: Exception) {
                loading.postValue(false)
                messageStringId.postValue(Resource.error(R.string.try_again))
            }
        }
    }

    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>) {
        viewModelScope.launch {
            try {
                val uploadedImageUrl = photoRepository.uploadPhoto(imageFile, user)
                val createdPost = postRepository.createPost(uploadedImageUrl, imageSize.first, imageSize.second, user)
                loading.postValue(false)
                post.postValue(Event(createdPost))
            } catch (e: Exception) {
                loading.postValue(false)
                handleNetworkError(e.cause)
            }
        }
    }
}