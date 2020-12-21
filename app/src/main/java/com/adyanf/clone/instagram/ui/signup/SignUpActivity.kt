package com.adyanf.clone.instagram.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.adyanf.clone.instagram.databinding.ActivitySignupBinding
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.login.LoginActivity
import com.adyanf.clone.instagram.ui.main.MainActivity
import com.adyanf.clone.instagram.utils.common.Status

class SignUpActivity : BaseActivity<SignUpViewModel, ActivitySignupBinding>() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    override fun provideViewBinding(layoutInflater: LayoutInflater): ActivitySignupBinding =
        ActivitySignupBinding.inflate(layoutInflater)

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btSignup.setOnClickListener { viewModel.onSignUpButtonClicked() }
        binding.btLogin.setOnClickListener { viewModel.onLoginButtonClicked() }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchMain.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        })

        viewModel.nameField.observe(this, Observer {
            if (binding.etName.text.toString() != it) binding.etName.setText(it)
        })

        viewModel.nameValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> binding.layoutName.error = it.data?.run { getString(this) }
                else -> binding.layoutName.isErrorEnabled = false
            }
        })

        viewModel.emailField.observe(this, Observer {
            if (binding.etEmail.text.toString() != it) binding.etEmail.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> binding.layoutEmail.error = it.data?.run { getString(this) }
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if (binding.etPassword.text.toString() != it) binding.etPassword.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> binding.layoutPassword.error = it.data?.run { getString(this) }
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        })

        viewModel.signingUp.observe(this, Observer {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
            binding.btSignup.isEnabled = !it
            binding.btLogin.isEnabled = !it
        })
    }
}