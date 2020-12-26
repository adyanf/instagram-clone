package com.adyanf.clone.instagram.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.adyanf.clone.instagram.databinding.ActivitySplashBinding
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.login.LoginActivity
import com.adyanf.clone.instagram.ui.main.MainActivity

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun provideViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this) {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }

        viewModel.launchLogin.observe(this) {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }
}