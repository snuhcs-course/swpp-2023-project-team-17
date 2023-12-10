package com.example.goclass.ui.mainui.usermain

import android.content.Context
import android.view.View
import android.widget.TimePicker
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.ui.mainui.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfessorMainFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    @Test
    fun createClassTest() {
        val scenario = setScenario(true, "professor")

//        onView(withId(R.id.createButton))
//            .perform(click())
//        onView(withId(R.id.dialog_create))
//            .check(matches(isDisplayed()))
//
//        onView(withId(R.id.classNameEdittext))
//            .perform(typeText("Test Create Class"))
//        onView(withId(R.id.buildingNumberEdittext))
//            .perform(typeText("300"))
//        onView(withId(R.id.roomNumberEditText))
//            .perform(typeText("201"))
//
//        onView(withId(R.id.startTimeButton))
//            .perform(click())
//        onView(withText("OK"))
//            .perform(click())
//
//        onView(withId(R.id.endTimeButton))
//            .perform(click())
//        onView(withText("OK"))
//            .perform(click())
//
//        onView(withId(R.id.codeEdittext))
//            .perform(typeText("fasjl"))
//        onView(withId(R.id.createButton))
//            .perform(click())

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

    fun pickTime(hour: Int, minute: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(
                    isAssignableFrom(android.widget.NumberPicker::class.java),
                    isDisplayed()
                )
            }

            override fun getDescription(): String {
                return "Set the time on a TimePicker"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val timePicker = view as TimePicker
                timePicker.hour = hour
                timePicker.minute = minute
            }
        }
    }
}
