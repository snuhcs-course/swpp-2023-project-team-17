package com.example.goclass.ui.classui.chats

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.goclass.R
import com.example.goclass.ui.classui.ClassActivity
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatCommentFragmentUITest {
    private val userId = 1
    private val userName = "abc"
    private val classId = 7
    private val className = "test class"

    @Test
    fun sendCommentTest() {
        val scenario = setScenario(true, "student")

        onView(withId(R.id.chatRecyclerView))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        onView(allOf(withId(R.id.commentText),
            isDisplayed(),
            isAssignableFrom(AppCompatEditText::class.java))
        ).perform(typeText("testing comment send"))
        onView(withId(R.id.commentSendButton))
            .perform(click())

        onView(withId(R.id.commentRecyclerView))
            .check(matches(hasDescendant(withText("testing comment send"))))

        scenario?.close()
    }

    @Test
    fun clickBackButton() {
        val scenario = setScenario(true, "student")

        onView(withId(R.id.chatRecyclerView))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        onView(withId(R.id.backButton))
            .perform(click())

        onView(withId(R.id.chatSendButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chatText))
            .check(matches(withText("")))
        onView(withId(R.id.chatui))
            .check(matches(isDisplayed()))
        onView(withId(R.id.attendanceButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.className))
            .check(matches(withText(className)))
        onView(withId(R.id.backButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chatRecyclerView))
            .check(matches(isDisplayed()))

        scenario?.close()
    }


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
