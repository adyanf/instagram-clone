package com.adyanf.clone.instagram.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adyanf.clone.instagram.databinding.FragmentPhotoBinding
import com.adyanf.clone.instagram.di.component.FragmentComponent
import com.adyanf.clone.instagram.ui.base.BaseFragment

class PhotoFragment : BaseFragment<PhotoViewModel, FragmentPhotoBinding>() {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoBinding = FragmentPhotoBinding.inflate(inflater, container, false)

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

    }

    companion object {
        const val TAG = "PhotoFragment"

        fun newInstance(): PhotoFragment {
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}