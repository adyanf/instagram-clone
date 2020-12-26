package com.adyanf.clone.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainSharedViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val homeRedirection: MutableLiveData<Event<Boolean>> = MutableLiveData()

    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    override fun onCreate() {}

    fun onHomeRedirect() {
        homeRedirection.postValue(Event(true))
    }
}