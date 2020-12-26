package com.adyanf.clone.instagram.ui.login

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.TestComponentRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class LoginActivityTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val chain = RuleChain.outerRule(component)

    @Before
    fun setup() {
        launch(LoginActivity::class.java)
    }

    @Test
    fun testCheckViewsDisplay() {
        // then
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_login)).check(matches(isDisplayed()))
    }

    @Test
    fun givenValidEmailAndValidPwd_whenLogin_shouldOpenMainActivity() {
        // when
        val emailFieldMatcher = withId(R.id.et_email)
        val pwdFieldMatcher = withId(R.id.et_password)
        val loginButtonMatcher = withId(R.id.bt_login)

        onView(emailFieldMatcher).perform(
            typeText("test@mindorks.com"),
            closeSoftKeyboard()
        )
        onView(pwdFieldMatcher).perform(
            typeText("test123"),
            closeSoftKeyboard()
        )
        onView(loginButtonMatcher).perform(click())

        // then
        val bottomNavigationMatcher = withId(R.id.bottom_navigation)
        onView(bottomNavigationMatcher).check(matches(isDisplayed()))
    }
}