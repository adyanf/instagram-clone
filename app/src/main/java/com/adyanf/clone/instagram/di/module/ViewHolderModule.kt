package com.adyanf.clone.instagram.di.module

import androidx.lifecycle.LifecycleRegistry
import com.adyanf.clone.instagram.di.ViewModelScope
import com.adyanf.clone.instagram.ui.base.BaseItemViewHolder
import dagger.Module
import dagger.Provides

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *, *>) {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)
}