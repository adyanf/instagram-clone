package com.adyanf.clone.instagram.ui.dummy

import com.adyanf.clone.instagram.data.repository.DummyRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class DummyViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val dummyRepository: DummyRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        // do something
    }
}