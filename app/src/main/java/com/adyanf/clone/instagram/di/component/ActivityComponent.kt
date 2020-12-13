package com.adyanf.clone.instagram.di.component

import com.adyanf.clone.instagram.di.ActivityScope
import com.adyanf.clone.instagram.di.module.ActivityModule
import com.adyanf.clone.instagram.ui.dummy.DummyActivity
import com.adyanf.clone.instagram.ui.login.LoginActivity
import com.adyanf.clone.instagram.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: DummyActivity)

    fun inject(activity: LoginActivity)
}