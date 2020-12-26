package com.adyanf.clone.instagram.ui.photo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.databinding.FragmentPhotoBinding
import com.adyanf.clone.instagram.di.component.FragmentComponent
import com.adyanf.clone.instagram.ui.base.BaseFragment
import com.adyanf.clone.instagram.ui.main.MainSharedViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.mindorks.paracamera.Camera
import java.io.FileNotFoundException
import javax.inject.Inject

class PhotoFragment : BaseFragment<PhotoViewModel, FragmentPhotoBinding>() {

    @Inject
    lateinit var camera: Camera

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoBinding = FragmentPhotoBinding.inflate(inflater, container, false)

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.post.observe(this) {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }
        }
    }

    override fun setupView(view: View) {
        binding.viewGallery.setOnClickListener {
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RQ_GALLERY_PICK)
                }
        }

        binding.viewCamera.setOnClickListener {
            try {
                camera.takePicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RQ_GALLERY_PICK -> {
                    try {
                        intent?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.try_again)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.try_again)
                    }
                }
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
            }
        }
    }

    companion object {
        const val TAG = "PhotoFragment"
        const val RQ_GALLERY_PICK = 1001

        fun newInstance(): PhotoFragment {
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}