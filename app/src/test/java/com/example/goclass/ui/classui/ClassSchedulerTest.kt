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
        val dayOfWeek1 = 3
        val startHour1 = 15
        val startMinute1 = 30

        val result1 = classScheduler.uniqueRequestId(userId1, classId1, dayOfWeek1, startHour1, startMinute1)
        var expectedResult1 = userId1.hashCode()
        expectedResult1 = 31 * expectedResult1 + classId1.hashCode()
        expectedResult1 = 31 * expectedResult1 + dayOfWeek1
        expectedResult1 = 31 * expectedResult1 + startHour1
        expectedResult1 = 31 * expectedResult1 + startMinute1
        assertEquals(expectedResult1, result1)

        val userId2 = 3
        val classId2 = 4
        val dayOfWeek2 = 5
        val startHour2 = 19
        val startMinute2 = 0

        val result2 = classScheduler.uniqueRequestId(userId2, classId2, dayOfWeek2, startHour2, startMinute2)
        var expectedResult2 = userId2.hashCode()
        expectedResult2 = 31 * expectedResult2 + classId2.hashCode()
        expectedResult2 = 31 * expectedResult2 + dayOfWeek2
        expectedResult2 = 31 * expectedResult2 + startHour2
        expectedResult2 = 31 * expectedResult2 + startMinute2
        assertEquals(expectedResult2, result2)
    }
}
