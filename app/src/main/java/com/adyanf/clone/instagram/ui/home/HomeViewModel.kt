package com.adyanf.clone.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeViewModel(
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList: ArrayList<Post>
) : BaseViewModel(networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts : MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    var firstPostId: String? = null
    var lastPostId: String? = null

    private val user: User = userRepository.getCurrentUser()!!

    override fun onCreate() {
        fetchPosts()
    }

    private fun fetchPosts() {
        if (checkInternetConnectionWithMessage()) {
            viewModelScope.launch {
                loading.postValue(true)

                try {
                    val newPostList = withContext(Dispatchers.IO) {
                        postRepository.fetchHomePostList(user, firstPostId, lastPostId)
                    }
                    allPostList.addAll(newPostList)

                    firstPostId = allPostList.maxByOrNull { post -> post.createdAt.time }?.id
                    lastPostId = allPostList.minByOrNull { post -> post.createdAt.time }?.id

                    loading.postValue(false)
                    posts.postValue(Resource.success(newPostList))
                } catch (e: Exception) {
                    loading.postValue(false)
                    handleNetworkError(e.cause)
                }
            }
        }
    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) fetchPosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        refreshPosts.postValue(
            Resource.success(mutableListOf<Post>().apply { addAll(allPostList) })
        )
    }
}