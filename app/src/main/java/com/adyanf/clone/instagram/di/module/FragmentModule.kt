package com.adyanf.clone.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyanf.clone.instagram.data.repository.DummyRepository
import com.adyanf.clone.instagram.data.repository.PostRepository
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseFragment
import com.adyanf.clone.instagram.ui.dummies.DummiesAdapter
import com.adyanf.clone.instagram.ui.dummies.DummiesViewModel
import com.adyanf.clone.instagram.ui.home.HomeViewModel
import com.adyanf.clone.instagram.ui.home.post.PostsAdapter
import com.adyanf.clone.instagram.ui.photo.PhotoViewModel
import com.adyanf.clone.instagram.ui.profile.ProfileViewModel
import com.adyanf.clone.instagram.utils.ViewModelProviderFactory
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideDummiesViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummiesViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(DummiesViewModel::class) {
                DummiesViewModel(schedulerProvider, compositeDisposable, networkHelper, dummyRepository)
            }
        ).get(DummiesViewModel::class.java)

    @Provides
    fun provideDummiesAdapter() = DummiesAdapter(fragment.lifecycle, ArrayList())

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
        ViewModelProviders.of(fragment,
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
        networkHelper: NetworkHelper
    ): PhotoViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(PhotoViewModel::class) {
                PhotoViewModel(schedulerProvider, compositeDisposable, networkHelper)
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
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(ProfileViewModel::class) {
                ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository, postRepository)
            }
        ).get(ProfileViewModel::class.java)
}