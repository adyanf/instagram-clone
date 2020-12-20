package com.adyanf.clone.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    postRepository: PostRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

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
    val name: LiveData<String> = Transformations.map(myInfo) { it.data?.name }
    val profileImage: LiveData<Image> = Transformations.map(myInfo) {
        it.data?.profilePicUrl?.run { Image(this, headers) }
    }
    val postCount: LiveData<Int> = Transformations.map(myPosts) { it.data?.size ?: 0 }
    val tagline: LiveData<String> = Transformations.map(myInfo) { it.data?.tagline }

    init {
        compositeDisposable.add(
            userRepository.doFetchInfo(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                       myInfo.postValue(Resource.success(it))
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
        compositeDisposable.add(
            postRepository.getMyPostList(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        myPosts.postValue(Resource.success(it))
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    override fun onCreate() {
        // do nothing
    }

    fun onClickLogout() {
        if (loading.value != true) {
            loading.postValue(true)
            compositeDisposable.add(
                userRepository.doUserLogout(user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            loading.postValue(false)
                            userRepository.removeCurrentUser()
                            launchLogin.postValue(Event(true))
                        },
                        {
                            loading.postValue(false)
                            handleNetworkError(it)
                        }
                    )
            )
        }
    }

    fun onClickEditProfile() {

    }
}