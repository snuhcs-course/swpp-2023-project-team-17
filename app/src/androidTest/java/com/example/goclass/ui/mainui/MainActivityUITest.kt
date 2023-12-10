package com.example.goclass.ui.mainui

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import org.junit.Test

class MainActivityUITest {
    private val userId = 1
    private val userName = "abc"

    @Test
    fun checkLogoutStatus() {
        val scenario = setScenario(false, "")

//        onView(withId(R.id.loginButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.textView))
//            .check(matches(withText("GoClass")))

        scenario?.close()
    }

    @Test
    fun checkProfileStatus() {
        val scenario = setScenario(true, "")

//        onView(withId(R.id.confirmButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.logoutButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.studentRadioButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.professorRadioButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.role))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.nameEditText))
//            .check(matches(withText(userName)))
//        onView(withId(R.id.name))
//            .check(matches(isDisplayed()))

        scenario?.close()
    }

    @Test
    fun checkStudentLoginStatus() {
        val scenario = setScenario(true, "student")

//        onView(withId(R.id.studentClassRecyclerView))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.joinButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.classListTextView))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.badge))
//            .check(matches(withText("Student")))
//        onView(withId(R.id.name))
//            .check(matches(withText(userName)))
//        onView(withId(R.id.profileButton))
//            .check(matches(isDisplayed()))

        scenario?.close()
    }

    @Test
    fun checkProfessorLoginStatus() {
        val scenario = setScenario(true, "professor")

//        onView(withId(R.id.professorClassRecyclerView))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.createButton))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.classListTextView))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.badge))
//            .check(matches(withText("Professor")))
//        onView(withId(R.id.name))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.profileButton))
//            .check(matches(isDisplayed()))

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
