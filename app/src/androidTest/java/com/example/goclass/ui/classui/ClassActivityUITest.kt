package com.example.goclass.ui.classui

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.goclass.R
import org.junit.Test

class ClassActivityUITest {
    @Test
    fun checkChatChannelDisplayed() {
        val classId = 7
        val className = "test class"

        val intent = Intent(ApplicationProvider.getApplicationContext(), ClassActivity::class.java)
        intent.putExtra("classId", classId)
        intent.putExtra("className", className)
        val scenario = ActivityScenario.launch<ClassActivity>(intent)

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

        scenario.close()
    }
}
