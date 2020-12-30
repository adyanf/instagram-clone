package com.adyanf.clone.instagram.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.adyanf.clone.instagram.InstagramApplication
import com.adyanf.clone.instagram.data.local.db.DatabaseService
import com.adyanf.clone.instagram.data.remote.FakeNetworkService
import com.adyanf.clone.instagram.data.remote.NetworkService
import com.adyanf.clone.instagram.data.remote.Networking
import com.adyanf.clone.instagram.utils.common.FileUtils
import com.adyanf.clone.instagram.utils.network.FakeNetworkHelperImpl
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class ApplicationTestModule(private val application: InstagramApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideTempDirectory(): File = FileUtils.getDirectory(application, "temp")

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("bootcamp-instagram-project-prefs", Context.MODE_PRIVATE)

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService =
        Room.databaseBuilder(
            application, DatabaseService::class.java,
            "bootcamp-instagram-project-db"
        ).build()

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService {
        Networking.API_KEY = "FAKE_API_KEY"
        return FakeNetworkService()
    }

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = FakeNetworkHelperImpl(application)
}