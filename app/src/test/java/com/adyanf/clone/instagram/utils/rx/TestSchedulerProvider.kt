package com.adyanf.clone.instagram.utils.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler): SchedulerProvider {
    override fun computation(): Scheduler = testScheduler
    override fun io(): Scheduler = testScheduler
    override fun ui(): Scheduler = testScheduler
}