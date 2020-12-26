package com.adyanf.clone.instagram.ui.profile.edit

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.databinding.ActivityEditProfileBinding
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.utils.common.GlideHelper
import com.adyanf.clone.instagram.utils.display.LoadingDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EditProfileActivity : BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>() {

    override fun provideViewBinding(layoutInflater: LayoutInflater): ActivityEditProfileBinding =
        ActivityEditProfileBinding.inflate(layoutInflater)

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this) {
            if (binding.etName.text.toString() != it) binding.etName.setText(it)
        }
        viewModel.bio.observe(this) {
            if (binding.etBio.text.toString() != it) binding.etBio.setText(it)
        }
        viewModel.email.observe(this) {
            if (binding.etEmail.text.toString() != it) binding.etEmail.setText(it)
        }
        viewModel.profilePicUrl.observe(this) {
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
            } ?: binding.ivProfile.setImageResource(R.drawable.ic_profile_add_pic)
        }
        viewModel.close.observe(this) {
            it.getIfNotHandled()?.run {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
        viewModel.updated.observe(this) {
            it.getIfNotHandled()?.run {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        viewModel.loading.observe(this) {
            it.getIfNotHandled()?.let { isLoading ->
                if (isLoading) {
                    LoadingDialog.show(getString(R.string.saving_profile), supportFragmentManager)
                } else {
                    LoadingDialog.dismiss()
                }
            }
        }
    }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onCloseClick()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    clearFocus()
                    viewModel.onSaveClick()
                    true
                }
                else -> false
            }
        }

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etBio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onBioChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun clearFocus() {
        binding.etName.clearFocus()
        binding.etBio.clearFocus()
    }
}