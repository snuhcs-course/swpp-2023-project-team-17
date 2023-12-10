/*
 * InputValidnessTest is a utility object that provides methods for validating input data related to user main operations.
 */

package com.example.goclass.ui.mainui.usermain.utils

import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

object InputValidnessTest {

    /*
     * isClassNameValid checks if the class name is valid.
     *
     * @param className: String, the class name to be validated.
     * @return Boolean, true if the class name is not empty after trimming, false otherwise.
     */
    fun isClassNameValid(className: String): Boolean {
        return className.trim().isNotEmpty()
    }

    /*
     * isClassTimeValid checks if the class times in the given container are valid.
     *
     * @param container: LinearLayout, the container holding the class time information.
     * @return Boolean, true if all class times are valid, false otherwise.
     */
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

    /*
     * isClassValid checks if the building number and room number are valid.
     *
     * @param buildingNumber: String, the building number to be validated.
     * @param roomNumber: String, the room number to be validated.
     * @return Boolean, true if both building number and room number are not empty after trimming, false otherwise.
     */
    fun isClassValid(buildingNumber: String, roomNumber: String): Boolean {
        return buildingNumber.trim().isNotEmpty() && roomNumber.trim().isNotEmpty()
    }

    /*
     * isClassCodeValid checks if the class code is valid.
     *
     * @param classCode: String, the class code to be validated.
     * @return Boolean, true if the class code is not empty after trimming, false otherwise.
     */
    fun isClassCodeValid(classCode: String): Boolean {
        return classCode.trim().isNotEmpty()
    }
}
