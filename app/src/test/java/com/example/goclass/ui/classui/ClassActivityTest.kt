package com.example.goclass.ui.classui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ClassActivityTest {

    private val context: Context = mockk()
    private val mockClassSharedPref: SharedPreferences = mockk()
    private val mockClassSharedPrefEditor: SharedPreferences.Editor = mockk()
    private val intent: Intent = mockk()

    private lateinit var classActivity: ClassActivity

    private val mockClassId = 123
    private val mockClassName = "TestClassName"

    @Before
    fun setUp() {
        coEvery { context.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE) } returns mockClassSharedPref
        coEvery { mockClassSharedPref.edit() } returns mockClassSharedPrefEditor
        coEvery { intent.getIntExtra("classId", -1) } returns mockClassId
        coEvery { intent.getStringExtra("className") } returns mockClassName

        coEvery { mockClassSharedPref.getInt("classId", -1) } returns mockClassId
        coEvery { mockClassSharedPref.getString("className", "") } returns mockClassName

        classActivity = ClassActivity()
    }

    @Test
    fun testSharedPreferences() {
        coEvery { mockClassSharedPrefEditor.putInt(any(), any()) } returns mockClassSharedPrefEditor
        coEvery { mockClassSharedPrefEditor.putString(any(), any()) } returns mockClassSharedPrefEditor
        coEvery { mockClassSharedPrefEditor.apply() } returns any()

        val classSharedPref = context.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = intent.getIntExtra("classId", -1)!!
        val className = intent.getStringExtra("className")!!
        with(classSharedPref?.edit()) {
            this?.putInt("classId", classId)
            this?.putString("className", className)
            this?.apply()
        }

        assertEquals(123, classSharedPref.getInt("classId", -1))
        assertEquals("TestClassName", classSharedPref.getString("className", ""))
    }

    // Add more test cases as needed
}