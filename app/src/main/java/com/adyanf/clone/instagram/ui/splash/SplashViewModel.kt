package com.adyanf.clone.instagram.ui.splash

import androidx.lifecycle.MutableLiveData
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.network.NetworkHelper

class SplashViewModel(
    networkHelper: NetworkHelper,
    val userRepository: UserRepository
) : BaseViewModel(networkHelper) {

    // Event is used by the view model to tell the activity to launch another Activity
    // view model also provided the Bundle in the event that is needed for the Activity
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    override fun onCreate() {
        if (userRepository.getCurrentUser() == null) {
            launchLogin.postValue(Event(emptyMap()))
        } else {
            launchMain.postValue(Event(emptyMap()))
        }
    }
}