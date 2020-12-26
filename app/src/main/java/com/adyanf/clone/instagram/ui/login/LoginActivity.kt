package com.adyanf.clone.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.adyanf.clone.instagram.databinding.ActivityLoginBinding
import com.adyanf.clone.instagram.di.component.ActivityComponent
import com.adyanf.clone.instagram.ui.base.BaseActivity
import com.adyanf.clone.instagram.ui.main.MainActivity
import com.adyanf.clone.instagram.ui.signup.SignUpActivity
import com.adyanf.clone.instagram.utils.common.Status

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun provideViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
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

        binding.btLogin.setOnClickListener { viewModel.onLoginButtonClicked() }
        binding.btSignup.setOnClickListener { viewModel.onSignUpButtonClicked() }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchMain.observe(this) {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }

        viewModel.launchSignUp.observe(this) {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
                finish()
            }
        }

        viewModel.emailField.observe(this) {
            if (binding.etEmail.text.toString() != it) binding.etEmail.setText(it)
        }

        viewModel.emailValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutEmail.error = it.data?.run { getString(this) }
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        }

        viewModel.passwordField.observe(this) {
            if (binding.etPassword.text.toString() != it) binding.etPassword.setText(it)
        }

        viewModel.passwordValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutPassword.error = it.data?.run { getString(this) }
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        }

        viewModel.loggingIn.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
            binding.btLogin.isEnabled = !it
            binding.btSignup.isEnabled = !it
        }
    }
}