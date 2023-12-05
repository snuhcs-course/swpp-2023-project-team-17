package com.example.goclass.ui.classui.attendances.service

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class AttendanceServiceTest {
    private val service = AttendanceService()

    /*@Test
    fun onDeviceFound_present() {
        val expectedAttendanceStatus = 2

        // Case 1
        var scanCount = 0
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 2
        scanCount = 5
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 3
        scanCount = 10
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)
    }

    @Test
    fun onDeviceFound_late() {
        val expectedAttendanceStatus = 1

        // Case 1
        var scanCount = 11
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 2
        scanCount = 20
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 3
        scanCount = 30
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)
    }

    @Test
    fun onDeviceFound_absent() {
        val expectedAttendanceStatus = 0

        // Case 1
        var scanCount = 31
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 2
        scanCount = 50
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)

        // Case 3
        scanCount = 70
        service.onDeviceFound(scanCount)
        TestCase.assertEquals(expectedAttendanceStatus, service.attendanceStatus)
    }*/
}
