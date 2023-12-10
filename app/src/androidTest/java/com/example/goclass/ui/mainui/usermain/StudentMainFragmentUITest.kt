package com.example.goclass.ui.mainui.usermain

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.ui.mainui.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentMainFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    @Test
    fun joinClassTest() {
        val scenario = setScenario(true, "student")

//        onView(withId(R.id.joinButton))
//            .perform(click())
//        onView(withId(R.id.dialog_join))
//            .check(matches(isDisplayed()))
//
//        onView(withId(R.id.nameEditText))
//            .perform(typeText("test class"))
//        onView(withId(R.id.codeEditText))
//            .perform(typeText("1234"))
//        onView(withId(R.id.joinButton))
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
}
