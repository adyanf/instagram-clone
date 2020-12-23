package com.adyanf.clone.instagram.ui.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.adyanf.clone.instagram.data.model.Image
import com.adyanf.clone.instagram.data.model.MyInfo
import com.adyanf.clone.instagram.data.remote.Networking
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val user = userRepository.getCurrentUser()!!
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )
    private val myInfo: MutableLiveData<MyInfo> = MutableLiveData()
    val close: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val updated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val name: LiveData<String> = Transformations.map(myInfo) { it.name }
    val bio: LiveData<String> = Transformations.map(myInfo) { it.tagline }
    val profilePicUrl: LiveData<Image> = Transformations.map(myInfo) {
        it.profilePicUrl?.run { Image(this, headers) }
    }
    val email: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        email.postValue(user.email)
        compositeDisposable.add(
            userRepository.doFetchInfo(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        myInfo.postValue(it)
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    fun onNameChange(newName: String) {
        myInfo.value?.let {
            val newMyInfo = MyInfo(
                id = it.id,
                name = newName,
                profilePicUrl = it.profilePicUrl,
                tagline = it.tagline
            )
            myInfo.postValue(newMyInfo)
        }
    }

    fun onBioChange(newBio: String) {
        myInfo.value?.let {
            val newMyInfo = MyInfo(
                id = it.id,
                name = it.name,
                profilePicUrl = it.profilePicUrl,
                tagline = newBio
            )
            myInfo.postValue(newMyInfo)
        }
    }

    fun onSaveClick() {
        myInfo.value?.let { newMyInfo ->
            loading.postValue(Event(true))
            compositeDisposable.add(
                userRepository.doUpdateInfo(user, newMyInfo)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            loading.postValue(Event(false))
                            userRepository.updateUserName(newMyInfo.name)
                            updated.postValue(Event(true))
                        },
                        {
                            loading.postValue(Event(false))
                            handleNetworkError(it)
                        }
                    )
            )
        }
    }

    fun onCloseClick() {
        close.postValue(Event(true))
    }
}