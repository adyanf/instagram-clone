package com.adyanf.clone.instagram.di.component

import com.adyanf.clone.instagram.di.FragmentScope
import com.adyanf.clone.instagram.di.module.FragmentModule
import com.adyanf.clone.instagram.ui.dummies.DummiesFragment
import com.adyanf.clone.instagram.ui.home.HomeFragment
import com.adyanf.clone.instagram.ui.photo.PhotoFragment
import com.adyanf.clone.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: PhotoFragment)

    fun inject(fragment: ProfileFragment)
}