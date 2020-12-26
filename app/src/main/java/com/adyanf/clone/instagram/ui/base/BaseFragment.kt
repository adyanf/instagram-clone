package com.adyanf.clone.instagram.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.adyanf.clone.instagram.InstagramApplication
import com.adyanf.clone.instagram.di.component.DaggerFragmentComponent
import com.adyanf.clone.instagram.di.component.FragmentComponent
import com.adyanf.clone.instagram.di.module.FragmentModule
import com.adyanf.clone.instagram.utils.display.Toaster
import javax.inject.Inject

/**
 * Reference for generics: https://kotlinlang.org/docs/reference/generics.html
 * Basically BaseFragment will take any class that extends BaseViewModel
 */
abstract class BaseFragment<VM : BaseViewModel, VB: ViewBinding> : Fragment() {

    @Inject
    lateinit var viewModel: VM

    private var _binding: VB? = null

    protected val binding: VB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        setupObservers()
        viewModel.onCreate()
    }

    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((context!!.applicationContext as InstagramApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun setupObservers() {
        viewModel.messageString.observe(this) {
            it.data?.run { showMessage(this) }
        }

        viewModel.messageStringId.observe(this) {
            it.data?.run { showMessage(this) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }


    fun showMessage(message: String) = context?.let { Toaster.show(it, message) }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    fun goBack() {
        if (activity is BaseActivity<*, *>) (activity as BaseActivity<*, *>).goBack()
    }

    protected abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    protected abstract fun setupView(view: View)
}

