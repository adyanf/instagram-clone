package com.adyanf.clone.instagram.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.databinding.FragmentProfileBinding
import com.adyanf.clone.instagram.di.component.FragmentComponent
import com.adyanf.clone.instagram.ui.base.BaseFragment
import com.adyanf.clone.instagram.ui.login.LoginActivity
import com.adyanf.clone.instagram.ui.profile.edit.EditProfileActivity
import com.adyanf.clone.instagram.utils.common.GlideHelper
import com.adyanf.clone.instagram.utils.display.Toaster
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this) {
            binding.tvName.text = it
        }

        viewModel.postCount.observe(this) {
            binding.tvPostsCount.text = Html.fromHtml(getString(R.string.posts_label, it))
        }

        viewModel.profileImage.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = binding.ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    binding.ivProfile.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(binding.ivProfile)
            }
        }

        viewModel.tagline.observe(this) {
            binding.tvTagline.text = it
        }

        viewModel.launchLogin.observe(this) {
            it.getIfNotHandled()?.run {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }

        viewModel.launchEditProfile.observe(this) {
            it.getIfNotHandled()?.run {
                startActivityForResult(Intent(context, EditProfileActivity::class.java), RQ_EDIT_PROFILE)
            }
        }
    }

    override fun setupView(view: View) {
        binding.btEditProfile.setOnClickListener {
            viewModel.onClickEditProfile()
        }

        binding.btLogout.setOnClickListener {
            viewModel.onClickLogout()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            context?.let {
                viewModel.fetchUserInfo()
                Toaster.show(it, "My Info Updated")
            }
        }
    }

    companion object {
        const val TAG = "ProfileFragment"
        const val RQ_EDIT_PROFILE = 10000

        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
}