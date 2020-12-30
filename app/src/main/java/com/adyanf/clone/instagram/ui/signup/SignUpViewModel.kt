package com.adyanf.clone.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.common.Status
import com.adyanf.clone.instagram.utils.common.Validation
import com.adyanf.clone.instagram.utils.common.Validator
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import java.lang.Exception

class SignUpViewModel(
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(networkHelper) {

    val nameField: MutableLiveData<String> = MutableLiveData()
    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val signingUp: MutableLiveData<Boolean> = MutableLiveData()

    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    val nameValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.NAME)
    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    override fun onCreate() { /* do nothing */}

    fun onNameChange(name: String) = nameField.postValue(name)

    fun onEmailChange(email: String) = emailField.postValue(email)

    fun onPasswordChange(password: String) = passwordField.postValue(password)

    fun onSignUpButtonClicked() {
        val name = nameField.value
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateRegisterFields(name, email, password)
        validationsList.postValue(validations)

        if (validations.isNotEmpty() && name != null && email != null && password != null) {
            val successValidations = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidations.size == validations.size && checkInternetConnectionWithMessage()) {
                doSignUp(name, email, password)
            }
        }
    }

    private fun doSignUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            signingUp.postValue(true)
            try {
                val signedUser = userRepository.doUserSignUp(name, email, password)
                userRepository.saveCurrentUser(signedUser)
                signingUp.postValue(false)
                launchMain.postValue(Event(emptyMap()))
            } catch (e: Exception) {
                signingUp.postValue(false)
                handleNetworkError(e.cause)
            }
        }
    }

    fun onLoginButtonClicked() {
        launchLogin.postValue(Event(emptyMap()))
    }

    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationsList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run this.resource }
                ?: Resource.unknown()
        }
}