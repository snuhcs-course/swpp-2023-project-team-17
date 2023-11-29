package com.example.goclass.ui.mainui.login

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.goclass.R
import com.example.goclass.ui.mainui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
class LoginFragmentTest {
    @Before
    fun setUp() {
        Intents.init()
    }
    @After
    fun tearDown() {
        Intents.release()
    }
    @Test
    fun testLoginFragment() {
        var scenario = ActivityScenario.launch(MainActivity::class.java)
        Intents.intending(IntentMatchers.hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
            .respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())
            )

        onView(withId(R.id.loginButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))

        scenario.close()
    }
}
