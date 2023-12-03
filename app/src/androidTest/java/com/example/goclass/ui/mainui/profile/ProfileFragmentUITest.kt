package com.example.goclass.ui.mainui.profile

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.goclass.R
import com.example.goclass.ui.mainui.MainActivity
import org.junit.Test
class ProfileFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    @Test
    fun editToStudentTest() {
        var scenario = setScenario(true, "")

        onView(withId(R.id.studentRadioButton))
            .perform(click())
        onView(withId(R.id.confirmButton))
            .perform(click())

        onView(withId(R.id.studentClassRecyclerView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.joinButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.classListTextView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.badge))
            .check(matches(withText("Student")))
        onView(withId(R.id.name))
            .check(matches(withText(userName)))
        onView(withId(R.id.profileButton))
            .check(matches(isDisplayed()))

        scenario?.close()
    }

    @Test
    fun editToProfessorTest() {
        var scenario = setScenario(true, "")

        onView(withId(R.id.professorRadioButton))
            .perform(click())
        onView(withId(R.id.confirmButton))
            .perform(click())

        onView(withId(R.id.professorClassRecyclerView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.createButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.classListTextView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.badge))
            .check(matches(withText("Professor")))
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.profileButton))
            .check(matches(isDisplayed()))

        scenario?.close()
    }

    @Test
    fun logoutYesButtonClickTest() {
        val scenario = setScenario(true, "")

        onView(withId(R.id.logoutButton))
            .perform(click())

        onView(withId(R.id.checkText))
            .check(matches(withText("Are you sure you want to logout?")))
        onView(withId(R.id.noButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.yesButton))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.textView))
            .check(matches(withText("GoClass")))

        scenario?.close()
    }

    @Test
    fun logoutNoButtonClickTest() {
        val scenario = setScenario(true, "")

        onView(withId(R.id.logoutButton))
            .perform(click())

        onView(withId(R.id.checkText))
            .check(matches(withText("Are you sure you want to logout?")))
        onView(withId(R.id.yesButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.noButton))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.confirmButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.logoutButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.studentRadioButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.professorRadioButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.role))
            .check(matches(isDisplayed()))
        onView(withId(R.id.nameEditText))
            .check(matches(withText(userName)))
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))

        scenario?.close()
    }

    private fun setScenario(isLoggedIn: Boolean, userRole: String): ActivityScenario<MainActivity>? {
        var scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity {
            val sharedPref = it.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref.edit()
                .putBoolean("isLoggedIn", isLoggedIn)
                .putString("userRole", userRole)
                .putInt("userId", userId)
                .putString("userName", userName)
                .apply()
        }
        scenario?.close()

        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)

        return scenario
    }
}
