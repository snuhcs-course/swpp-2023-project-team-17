package com.example.goclass.ui.classui

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ClassSchedulerTest {
    private lateinit var classScheduler: ClassScheduler
    @Before
    fun setUp() {
        classScheduler = ClassScheduler()
    }
    @Test
    fun uniqueRequestId_test() {
        val userId1 = 1
        val classId1 = 2
        val result1 = classScheduler.uniqueRequestId(userId1, classId1)
        assertEquals(userId1.hashCode() * 31 + classId1.hashCode(), result1)

        val userId2 = 3
        val classId2 = 4
        val result2 = classScheduler.uniqueRequestId(userId2, classId2)
        assertEquals(userId2.hashCode() * 31 + classId2.hashCode(), result2)

    }
}
