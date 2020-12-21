package com.adyanf.clone.instagram.ui.dummy

import android.os.Bundle
import android.view.LayoutInflater
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.databinding.ActivityDummyBinding
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.dummies.DummiesFragment

class DummyActivity : BaseActivity<DummyViewModel, ActivityDummyBinding>() {

    companion object {
        const val TAG = "DummyActivity"
    }

    override fun provideViewBinding(layoutInflater: LayoutInflater): ActivityDummyBinding =
        ActivityDummyBinding.inflate(layoutInflater)

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        addDummiesFragment()
    }

    private fun addDummiesFragment() {
        supportFragmentManager.findFragmentByTag(DummiesFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, DummiesFragment.newInstance(), DummiesFragment.TAG)
            .commitAllowingStateLoss()
    }
}