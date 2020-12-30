package com.adyanf.clone.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.adyanf.clone.instagram.data.model.Image
import com.adyanf.clone.instagram.data.model.MyInfo
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.remote.Networking
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.launch

class ProfileViewModel(
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(networkHelper) {

    private val user = userRepository.getCurrentUser()!!
    private val myInfo: MutableLiveData<Resource<MyInfo>> = MutableLiveData()
    private val myPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val launchLogin: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val launchEditProfile: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val name: LiveData<String> = Transformations.map(myInfo) { it.data?.name }
    val profileImage: LiveData<Image> = Transformations.map(myInfo) {
        it.data?.profilePicUrl?.run { Image(this, headers) }
    }
    val postCount: LiveData<Int> = Transformations.map(myPosts) { it.data?.size ?: 0 }
    val tagline: LiveData<String> = Transformations.map(myInfo) { it.data?.tagline }

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val myInfoResponse = userRepository.doFetchInfo(user)
                myInfo.postValue(Resource.success(myInfoResponse))
            } catch (e: Exception) {
                handleNetworkError(e.cause)
            }
        }
    }

    private fun fetchUserPost() {
        viewModelScope.launch {
            try {
                val myPostList = postRepository.getMyPostList(user)
                myPosts.postValue(Resource.success(myPostList))
            } catch (e: Exception) {
                handleNetworkError(e.cause)
            }
        }
    }

    override fun onCreate() {
        fetchUserInfo()
        fetchUserPost()
    }

    fun onClickLogout() {
        if (loading.value != true) {
            viewModelScope.launch {
                loading.postValue(true)
                try {
                    userRepository.doUserLogout(user)
                    loading.postValue(false)
                    userRepository.removeCurrentUser()
                    launchLogin.postValue(Event(true))
                } catch (e: Exception) {
                    loading.postValue(false)
                    handleNetworkError(e.cause)
                }
            }
        }
    }

    fun onClickEditProfile() {
        launchEditProfile.postValue(Event(true))
    }
}