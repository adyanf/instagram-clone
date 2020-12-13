package com.adyanf.clone.instagram.di.component

import com.adyanf.clone.instagram.di.FragmentScope
import com.adyanf.clone.instagram.di.module.FragmentModule
import com.adyanf.clone.instagram.ui.dummies.DummiesFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)
}