package com.example.goclass.ui.classui.attendances

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.R
import com.example.goclass.ui.classui.ClassActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AttendanceFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    @Test
    fun viewAttendanceList() {
        val scenario = setScenario(true, "professor")

        onView(withId(R.id.attendanceButton))
            .perform(click())
        onView(withId(R.id.professorAttendanceRecyclerView))
            .check(matches(isDisplayed()))

        scenario?.close()
    }

    private fun setScenario(isLoggedIn: Boolean, userRole: String): ActivityScenario<ClassActivity>? {
        val classId = 8
        val className = "test class"

        val intent = Intent(ApplicationProvider.getApplicationContext(), ClassActivity::class.java)
        intent.putExtra("classId", classId)
        intent.putExtra("className", className)
        var scenario = ActivityScenario.launch<ClassActivity>(intent)

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

        scenario = ActivityScenario.launch<ClassActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        return scenario
    }
}
