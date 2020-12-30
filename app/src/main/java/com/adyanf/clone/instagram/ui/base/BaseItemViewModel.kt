package com.adyanf.clone.instagram.ui.base

import androidx.lifecycle.MutableLiveData
import com.adyanf.clone.instagram.utils.network.NetworkHelper

abstract class BaseItemViewModel<T : Any>(
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper) {

    val data: MutableLiveData<T> = MutableLiveData()

    fun onManualCleared() = onCleared()

    fun updateData(data: T) {
        this.data.postValue(data)
    }
}