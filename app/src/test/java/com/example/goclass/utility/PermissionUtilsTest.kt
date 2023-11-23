package com.example.goclass.utility

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PermissionUtilsTest {
    private lateinit var bluetoothPermission: String
    private lateinit var permissionUtils: PermissionUtils
    private val context: Activity = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bluetoothPermission = Manifest.permission.BLUETOOTH
        permissionUtils = PermissionUtils(context)
    }

    @Test
    fun requestBluetoothPermissions_test_permission_granted() {
        // Mock Bluetooth permission granted
        val activity: Activity = mockk()
        val requestCode = 123
        every {
            ContextCompat.checkSelfPermission(activity, bluetoothPermission)
        } returns PackageManager.PERMISSION_GRANTED

        val result = permissionUtils.requestBluetoothPermissions(activity, requestCode)

        // Verify that the function returns true when Bluetooth permission is granted
        Assert.assertTrue(result)
    }

    @Test
    fun requestBluetoothPermissions_test_permission_not_granted() {
        // Mock Bluetooth permission granted
        val activity: Activity = mockk()
        val requestCode = 123
        every {
            ContextCompat.checkSelfPermission(activity, bluetoothPermission)
        } returns PackageManager.PERMISSION_DENIED

        val result = permissionUtils.requestBluetoothPermissions(activity, requestCode)

        // Verify that the function returns true when Bluetooth permission is granted
        Assert.assertFalse(result)
    }
}
