package com.adyanf.clone.instagram

import android.app.Application
import com.adyanf.clone.instagram.di.component.ApplicationComponent
import com.adyanf.clone.instagram.di.component.DaggerApplicationComponent
import com.adyanf.clone.instagram.di.module.ApplicationModule

class InstagramApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}