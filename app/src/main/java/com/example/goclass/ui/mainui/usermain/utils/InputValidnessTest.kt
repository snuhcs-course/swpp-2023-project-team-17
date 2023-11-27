package com.example.goclass.ui.mainui.usermain.utils

import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

object InputValidnessTest {
    fun isClassNameValid(className: String): Boolean {
        return className.trim().isNotEmpty()
    }

    fun isClassTimeValid(container: LinearLayout): Boolean {
        for (i in 0 until container.childCount) {
            val layout = container.getChildAt(i) as ConstraintLayout
            val startTimeButton = layout.findViewWithTag<Button>("startTimeButton")
            val endTimeButton = layout.findViewWithTag<Button>("endTimeButton")

            val startTime = startTimeButton.text.toString()
            val endTime = endTimeButton.text.toString()

            if (startTime == "Start Time" || endTime == "End Time") {
                return false
            }

            if (startTime >= endTime) {
                return false
            }
        }
        return true
    }

    fun isClassValid(buildingNumber:String, roomNumber:String): Boolean {
        return buildingNumber.trim().isNotEmpty() && roomNumber.trim().isNotEmpty()
    }

    fun isClassCodeValid(classCode:String): Boolean {
        return classCode.trim().isNotEmpty()
    }
}