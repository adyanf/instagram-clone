package com.adyanf.clone.instagram.di.component

import com.adyanf.clone.instagram.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent