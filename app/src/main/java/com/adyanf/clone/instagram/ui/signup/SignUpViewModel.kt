package com.adyanf.clone.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.ui.base.BaseViewModel
import com.adyanf.clone.instagram.utils.common.*
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import com.adyanf.clone.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val nameField: MutableLiveData<String> = MutableLiveData()
    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val signingUp: MutableLiveData<Boolean> = MutableLiveData()

    val launchDummy: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
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
                signingUp.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserSignUp(name, email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                userRepository.saveCurrentUser(it)
                                signingUp.postValue(false)
                                launchDummy.postValue(Event(emptyMap()))
                            },
                            {
                                handleNetworkError(it)
                                signingUp.postValue(false)
                            }
                        )
                )
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