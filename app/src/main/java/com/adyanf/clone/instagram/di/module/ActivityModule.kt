package com.adyanf.clone.instagram.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.login.LoginViewModel
import com.adyanf.clone.instagram.ui.main.MainSharedViewModel
import com.adyanf.clone.instagram.ui.main.MainViewModel
import com.adyanf.clone.instagram.ui.profile.edit.EditProfileViewModel
import com.adyanf.clone.instagram.ui.signup.SignUpViewModel
import com.adyanf.clone.instagram.ui.splash.SplashViewModel
import com.adyanf.clone.instagram.utils.ViewModelProviderFactory
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import dagger.Module
import dagger.Provides

/**
 * Kotlin Generics Reference: https://kotlinlang.org/docs/reference/generics.html
 * Basically it means that we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 */
@Module
class ActivityModule(private val activity: BaseActivity<*, *>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideSplashViewModel(
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(networkHelper, userRepository)
            //this lambda creates and return SplashViewModel
        }).get(SplashViewModel::class.java)

    @Provides
    fun provideLoginViewModel(
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(networkHelper, userRepository)
        }).get(LoginViewModel::class.java)

    @Provides
    fun provideSignUpViewModel(
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SignUpViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(SignUpViewModel::class) {
            SignUpViewModel(networkHelper, userRepository)
        }).get(SignUpViewModel::class.java)

    @Provides
    fun provideMainViewModel(
        networkHelper: NetworkHelper
    ): MainViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(networkHelper)
        }).get(MainViewModel::class.java)

    @Provides
    fun provideEditProfileViewModel(
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): EditProfileViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(EditProfileViewModel::class) {
            EditProfileViewModel(networkHelper, userRepository)
        }).get(EditProfileViewModel::class.java)

    @Provides
    fun provideMainSharedViewModel(
        networkHelper: NetworkHelper
    ): MainSharedViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(networkHelper)
        }).get(MainSharedViewModel::class.java)
}