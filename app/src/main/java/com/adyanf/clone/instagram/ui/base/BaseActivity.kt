package com.adyanf.clone.instagram.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.adyanf.clone.instagram.InstagramApplication
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.di.component.DaggerActivityComponent
import com.adyanf.clone.instagram.di.module.ActivityModule
import com.adyanf.clone.instagram.utils.display.Toaster
import javax.inject.Inject

/**
 * Reference for generics: https://kotlinlang.org/docs/reference/generics.html
 * Basically BaseActivity will take any class that extends BaseViewModel
 */
abstract class BaseActivity<VM : BaseViewModel, VB: ViewBinding> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildActivityComponent())
        super.onCreate(savedInstanceState)
        binding = provideViewBinding(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()
    }

    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as InstagramApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()

    protected open fun setupObservers() {
        viewModel.messageString.observe(this) {
            it.data?.run { showMessage(this) }
        }

        viewModel.messageStringId.observe(this) {
            it.data?.run { showMessage(this) }
        }
    }

    fun showMessage(message: String) = Toaster.show(applicationContext, message)

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    open fun goBack() = onBackPressed()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    protected abstract fun provideViewBinding(layoutInflater: LayoutInflater): VB

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected abstract fun setupView(savedInstanceState: Bundle?)
}