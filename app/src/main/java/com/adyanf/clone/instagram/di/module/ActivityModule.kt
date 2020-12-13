package com.adyanf.clone.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyanf.clone.instagram.data.repository.DummyRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.dummy.DummyViewModel
import com.adyanf.clone.instagram.ui.login.LoginViewModel
import com.adyanf.clone.instagram.ui.splash.SplashViewModel
import com.adyanf.clone.instagram.utils.ViewModelProviderFactory
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Kotlin Generics Reference: https://kotlinlang.org/docs/reference/generics.html
 * Basically it means that we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 */
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
            //this lambda creates and return SplashViewModel
        }).get(SplashViewModel::class.java)

    @Provides
    fun provideDummyViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummyViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(DummyViewModel::class) {
            DummyViewModel(schedulerProvider, compositeDisposable, networkHelper, dummyRepository)
        }).get(DummyViewModel::class.java)

    @Provides
    fun provideDLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(LoginViewModel::class.java)
}