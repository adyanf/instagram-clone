package com.adyanf.clone.instagram.ui.home.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.data.model.Image
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.remote.Networking
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseItemViewModel
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.common.TimeUtils
import com.adyanf.clone.instagram.utils.display.ScreenUtils
import com.adyanf.clone.instagram.utils.log.Logger
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    private val user = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val name: LiveData<String> = Transformations.map(data) { it.creator.name }
    val postTime: LiveData<String> = Transformations.map(data) { TimeUtils.getTimeAgo(it.createdAt) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.size ?: 0 }
    val isLiked: LiveData<Boolean> = Transformations.map(data) { it.likedBy?.any { postUser ->
        postUser.id == user.id
    }}
    val profileImage: LiveData<Image> = Transformations.map(data) {
        it.creator.profilePicUrl?.run { Image(this, headers) }
    }
    val imageDetail: LiveData<Image> = Transformations.map(data) {
        Image(
            it.imageUrl,
            headers,
            screenWidth,
            it.imageHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3
        )
    }

    private fun calculateScaleFactor(post: Post): Float {
        return post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f
    }

    override fun onCreate() {
        Logger.d(TAG, "onCreate Failed")
    }

    fun onLikeClick() = data.value?.let {
        if (networkHelper.isNetworkConnected()) {
            val api = if (isLiked.value == true) postRepository.makeUnlikePost(it, user)
                else postRepository.makeLikePost(it, user)

            compositeDisposable.add(
                api
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        { responsePost -> if (responsePost.id == it.id) updateData(responsePost) },
                        { error -> handleNetworkError(error) }
                    )
            )
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

    companion object {
        const val TAG = "PostItemViewModel"
    }
}