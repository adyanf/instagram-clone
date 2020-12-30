package com.adyanf.clone.instagram.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.repository.UserRepository
import com.adyanf.clone.instagram.utils.common.Event
import com.adyanf.clone.instagram.utils.common.Resource
import com.adyanf.clone.instagram.utils.coroutine.TestCoroutineRule
import com.adyanf.clone.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var loggingInObserver: Observer<Boolean>

    @Mock
    private lateinit var launchMainObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(networkHelper, userRepository)
        loginViewModel.loggingIn.observeForever(loggingInObserver)
        loginViewModel.launchMain.observeForever(launchMainObserver)
        loginViewModel.messageStringId.observeForever(messageStringIdObserver)
    }

    @After
    fun teardown() {
        loginViewModel.loggingIn.removeObserver(loggingInObserver)
        loginViewModel.launchMain.removeObserver(launchMainObserver)
        loginViewModel.messageStringId.removeObserver(messageStringIdObserver)
    }

    @Test
    fun givenServerResponse200_whenLogin_shouldLaunchDummyActivity() = testCoroutineRule.runBlockingTest {
        // given
        val email = "test@gmail.com"
        val password = "password"
        val user = User("id", "test", email, "accessToken")
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(user)
            .`when`(userRepository)
            .doUserLogin(email, password)

        loginViewModel.onLoginButtonClicked()

        verify(userRepository).saveCurrentUser(user)
        assert(loginViewModel.loggingIn.value == false)
        assert(loginViewModel.launchMain.value == Event(hashMapOf<String, String>()))
        verify(loggingInObserver).onChanged(true)
        verify(loggingInObserver).onChanged(false)
        verify(launchMainObserver).onChanged(Event(hashMapOf()))
    }

    @Test
    fun givenNoInternet_whenLogin_shouldShowNetworkError() = testCoroutineRule.runBlockingTest {
        val email = "test@gmail.com"
        val password = "password"
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        loginViewModel.onLoginButtonClicked()

        assert(loginViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        verify(messageStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
        verify(loggingInObserver, never()).onChanged(true)
    }
}