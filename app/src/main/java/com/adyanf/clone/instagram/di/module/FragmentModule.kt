package com.adyanf.clone.instagram.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyanf.clone.instagram.data.repository.PhotoRepository
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.di.TempDirectory
import com.adyanf.clone.instagram.ui.base.BaseFragment
import com.adyanf.clone.instagram.ui.home.HomeViewModel
import com.adyanf.clone.instagram.ui.home.post.PostsAdapter
import com.adyanf.clone.instagram.ui.main.MainSharedViewModel
import com.adyanf.clone.instagram.ui.photo.PhotoViewModel
import com.adyanf.clone.instagram.ui.profile.ProfileViewModel
import com.adyanf.clone.instagram.utils.ViewModelProviderFactory
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import com.mindorks.paracamera.Camera
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import java.io.File

@Module
class FragmentModule(private val fragment: BaseFragment<*, *>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideCamera(): Camera = Camera.Builder()
        .resetToCorrectOrientation(true) // it will rotate the camera bitmap to the correct orientation from meta data
        .setTakePhotoRequestCode(1)
        .setDirectory("temp")
        .setName("camera_temp_img")
        .setImageFormat(Camera.IMAGE_JPEG)
        .setCompression(75)
        .setImageHeight(500)// it will try to achieve this height as close as possible maintaining the aspect ratio;
        .build(fragment)

    @Provides
    fun providePostsAdapter() = PostsAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): HomeViewModel =
        ViewModelProvider(fragment,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    userRepository,
                    postRepository,
                    ArrayList(),
                    PublishProcessor.create()
                )
            }
        ).get(HomeViewModel::class.java)

    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        postRepository: PostRepository,
        @TempDirectory directory: File
    ): PhotoViewModel =
        ViewModelProvider(fragment,
            ViewModelProviderFactory(PhotoViewModel::class) {
                PhotoViewModel(
                    schedulerProvider, compositeDisposable, networkHelper,
                    userRepository, photoRepository, postRepository, directory
                )
            }
        ).get(PhotoViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): ProfileViewModel =
        ViewModelProvider(fragment,
            ViewModelProviderFactory(ProfileViewModel::class) {
                ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository, postRepository)
            }
        ).get(ProfileViewModel::class.java)

    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel = ViewModelProvider(
        fragment.activity!!, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(MainSharedViewModel::class.java)
}