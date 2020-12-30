package com.adyanf.clone.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.network.NetworkHelper

class MainViewModel(
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper) {

    val homeNavigation = MutableLiveData<Event<Boolean>>()
    val photoNavigation = MutableLiveData<Event<Boolean>>()
    val profileNavigation = MutableLiveData<Event<Boolean>>()

    override fun onCreate() {
        homeNavigation.postValue(Event(true))
    }

    fun onHomeSelected() {
        homeNavigation.postValue(Event(true))
    }

    fun onPhotoSelected() {
        photoNavigation.postValue(Event(true))
    }

    fun onProfileSelected() {
        profileNavigation.postValue(Event(true))
    }
}