package com.example.goclass.ui.classui.attendances.student

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.ui.classui.ClassActivity
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentAttendanceFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    private val classId = 7
    private val className = "test class"

//    @Test
//    fun clickAttendanceSendButton() {
//        val scenario = setScenario(true, "student")
//
//        onView(withId(R.id.attendanceButton))
//            .perform(click())
//        onView(withId(R.id.button))
//            .perform(click())
//        onView(withId(R.id.sendButton))
//            .check(matches(isDisplayed()))
//
//        scenario?.close()
//    }

    private fun setScenario(isLoggedIn: Boolean, userRole: String): ActivityScenario<ClassActivity>? {
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
