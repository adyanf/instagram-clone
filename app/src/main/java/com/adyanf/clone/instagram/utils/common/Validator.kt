package com.adyanf.clone.instagram.utils.common

import com.adyanf.clone.instagram.R
import java.util.regex.Pattern

object Validator {

    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    )
    private const val MIN_NAME_LENGTH = 3
    private const val MIN_PASSWORD_LENGTH = 6

    fun validateLoginFields(email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            add(validateEmailField(email))
            add(validatePasswordField(password))
        }

    fun validateRegisterFields(name: String?, email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            add(validateNameField(name))
            add(validateEmailField(email))
            add(validatePasswordField(password))
        }

    private fun validateNameField(name: String?): Validation = when {
        name.isNullOrBlank() ->
            Validation(Validation.Field.NAME, Resource.error(R.string.name_field_empty))
        name.length < MIN_NAME_LENGTH ->
            Validation(Validation.Field.NAME, Resource.error(R.string.name_field_small_length))
        else ->
            Validation(Validation.Field.NAME, Resource.success())
    }

    private fun validateEmailField(email: String?): Validation = when {
        email.isNullOrBlank() ->
            Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_empty))
        !EMAIL_ADDRESS.matcher(email).matches() ->
            Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid))
        else ->
            Validation(Validation.Field.EMAIL, Resource.success())
    }

    private fun validatePasswordField(password: String?): Validation = when {
        password.isNullOrBlank() ->
            Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty))
        password.length < MIN_PASSWORD_LENGTH ->
            Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
        else ->
            Validation(Validation.Field.PASSWORD, Resource.success())
    }
}

data class Validation(val field: Field, val resource: Resource<Int>) {
    enum class Field {
        EMAIL,
        PASSWORD,
        NAME
    }
}
