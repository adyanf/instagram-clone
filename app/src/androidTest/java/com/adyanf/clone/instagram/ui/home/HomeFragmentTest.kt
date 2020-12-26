package com.adyanf.clone.instagram.ui.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.TestComponentRule
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.utils.network.RecyclerViewMatcher.atPositionOnView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class HomeFragmentTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val chain = RuleChain.outerRule(component)

    @Before
    fun setup() {
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")
        val userRepository = component.testComponent!!.getUserRepository()
        userRepository.saveCurrentUser(user)
    }

    @Test
    fun postsAvailable_shouldDisplay() {
        launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)

        val recyclerViewMatcher = withId(R.id.rvPosts)
        onView(recyclerViewMatcher).check(matches(isDisplayed()))
        onView(recyclerViewMatcher)
            .check(matches(atPositionOnView(0, withText("name1"), R.id.tvName)))
        onView(recyclerViewMatcher)
            .perform(scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnView(1, withText("name2"), R.id.tvName)))
    }
}