package com.example.goclass.ui.classui.chats

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.R
import com.example.goclass.ui.classui.ClassActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    @Test
    fun sendChatTest() {
        val scenario = setScenario(true, "student")

        onView(withId(R.id.chatText))
            .perform(typeText("testing chat send"))
        onView(withId(R.id.chatSendButton))
            .perform(click())
        onView(withId(R.id.chatRecyclerView))
            .check(matches(hasDescendant(withText("testing chat send"))))

        scenario?.close()
    }

    private fun setScenario(isLoggedIn: Boolean, userRole: String): ActivityScenario<ClassActivity>? {
        val classId = 7
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
