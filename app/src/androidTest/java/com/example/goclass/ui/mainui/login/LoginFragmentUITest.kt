package com.example.goclass.ui.mainui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import com.example.goclass.ui.mainui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
class LoginFragmentUITest {
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
//        Intents.intending(IntentMatchers.hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
//            .respondWith(
//                Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())
//            )
//
//        onView(withId(R.id.loginButton)).perform(click())
//        Intents.intended(IntentMatchers.hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))

        scenario.close()
    }
}
