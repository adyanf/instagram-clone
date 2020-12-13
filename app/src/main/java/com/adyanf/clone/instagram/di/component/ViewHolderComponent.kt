package com.adyanf.clone.instagram.di.component

import com.adyanf.clone.instagram.di.ViewModelScope
import com.adyanf.clone.instagram.di.module.ViewHolderModule
import com.adyanf.clone.instagram.ui.dummies.DummyItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder: DummyItemViewHolder)
}