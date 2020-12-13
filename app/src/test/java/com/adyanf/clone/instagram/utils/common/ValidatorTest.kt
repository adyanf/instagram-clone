package com.adyanf.clone.instagram.utils.common

import com.adyanf.clone.instagram.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Test

class ValidatorTest {

    @Test
    fun givenValidEmailAndValidPwd_whenValidateLogin_shouldReturnSuccess() {
        // given
        val email = "test@gmail.com"
        val password = "password"

        // when
        val validations = Validator.validateLoginFields(email, password)

        // then
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenInvalidEmailAndValidPwd_whenValidateLogin_shouldReturnErrorEmail() {
        // given
        val email = "test@gmailcom"
        val password = "password"

        // when
        val validations = Validator.validateLoginFields(email, password)

        // then
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenValidEmailAndInvalidPwd_whenValidateLogin_shouldReturnErrorPwd() {
        // given
        val email = "test@gmail.com"
        val password = "pass"

        // when
        val validations = Validator.validateLoginFields(email, password)

        // then
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            )
        )
    }

    @Test
    fun givenInvalidEmailAndInvalidPwd_whenValidateLogin_shouldReturnErrorEmailAndErrorPwd() {
        // given
        val email = "test@gmailcom"
        val password = "pass"

        // when
        val validations = Validator.validateLoginFields(email, password)

        // then
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            )
        )
    }

    @Test
    fun givenEmptyEmailAndEmptyPwd_whenValidateLogin_shouldReturnErrorEmailAndErrorPwd() {
        // given
        val email = ""
        val password = ""

        // when
        val validations = Validator.validateLoginFields(email, password)

        // then
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_empty)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty))
            )
        )
    }

    @Test
    fun givenValidNameValidEmailAndValidPwd_whenValidateSignUp_shouldReturnSuccess() {
        // given
        val name = "Test"
        val email = "test@gmail.com"
        val password = "password"

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.success()),
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenInvalidNameValidEmailAndValidPwd_whenValidateSignUp_shouldReturnErrorName() {
        // given
        val name = "Te"
        val email = "test@gmail.com"
        val password = "password"

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.error(R.string.name_field_small_length)),
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenValidNameInvalidEmailAndValidPwd_whenValidateSignUp_shouldReturnErrorEmail() {
        // given
        val name = "Test"
        val email = "test@gmailcom"
        val password = "password"

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.success()),
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun givenValidNameValidEmailAndInvalidPwd_whenValidateSignUp_shouldReturnErrorPassword() {
        // given
        val name = "Test"
        val email = "test@gmail.com"
        val password = "pass"

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.success()),
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            )
        )
    }

    @Test
    fun givenInvalidNameInvalidEmailAndInvalidPwd_whenValidateSignUp_shouldReturnErrorNameEmailAndPwd() {
        // given
        val name = "Te"
        val email = "test@gmailcom"
        val password = "pass"

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.error(R.string.name_field_small_length)),
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            )
        )
    }

    @Test
    fun givenEmptyNameEmptyEmailAndEmptyPwd_whenValidateSignUp_shouldReturnErrorNameEmailAndPwd() {
        // given
        val name = ""
        val email = ""
        val password = ""

        // when
        val validations = Validator.validateRegisterFields(name, email, password)

        // then
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.NAME, Resource.error(R.string.name_field_empty)),
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_empty)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty))
            )
        )
    }
}