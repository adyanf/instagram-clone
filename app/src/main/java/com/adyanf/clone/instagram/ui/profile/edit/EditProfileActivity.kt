package com.adyanf.clone.instagram.ui.profile.edit

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.utils.common.GlideHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.ivProfile

class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            if (et_name.text.toString() != it) et_name.setText(it)
        })

        viewModel.bio.observe(this, Observer {
            if (et_bio.text.toString() != it) et_bio.setText(it)
        })
        viewModel.email.observe(this, Observer {
            if (et_email.text.toString() != it) et_email.setText(it)
        })
        viewModel.profilePicUrl.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    ivProfile.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(ivProfile)
            } ?: ivProfile.setImageResource(R.drawable.ic_profile_add_pic)
        })

        viewModel.close.observe(this, Observer {
            it.getIfNotHandled()?.run {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
        viewModel.updated.observe(this, Observer {
            it.getIfNotHandled()?.run {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    override fun setupView(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            viewModel.onCloseClick()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    clearFocus()
                    viewModel.onSaveClick()
                    true
                }
                else -> false
            }
        }

        et_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        et_bio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onBioChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun clearFocus() {
        et_name.clearFocus()
        et_bio.clearFocus()
    }
}