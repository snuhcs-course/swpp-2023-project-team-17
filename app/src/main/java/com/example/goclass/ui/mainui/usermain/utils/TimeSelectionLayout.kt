package com.example.goclass.ui.mainui.usermain.utils

import android.R
import android.app.TimePickerDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner

object TimeSelectionLayout {
    fun create(
        context: Context,
        deleteButtonRequired: Boolean = true
    ): LinearLayout {
        val timeSelectionLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL

            val daysOfWeek = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri")
            val dayAdapter = ArrayAdapter(
                context,
                R.layout.simple_spinner_item,
                daysOfWeek
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val dayDropdown = Spinner(context).apply {
                adapter = dayAdapter
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                tag = "dayDropdown"
            }
            addView(dayDropdown)

            val startTimeButton = Button(context).apply {
                text = "Start Time"
                tag = "startTimeButton"
            }
            addView(startTimeButton)

            val endTimeButton = Button(context).apply {
                text = "End Time"
                tag = "endTimeButton"
            }
            addView(endTimeButton)

            val timePickerListener = { buttonToUpdate: Button ->
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    buttonToUpdate.text = String.format("%02d:%02d", hourOfDay, minute)
                }
            }

            startTimeButton.setOnClickListener {
                TimePickerDialog(
                    context,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    timePickerListener(startTimeButton),
                    12,
                    0,
                    true
                ).show()
            }

            endTimeButton.setOnClickListener {
                TimePickerDialog(
                    context,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    timePickerListener(endTimeButton),
                    12,
                    0,
                    true
                ).show()
            }
        }

        if (deleteButtonRequired) {
            val deleteButton = ImageButton(context).apply {
                setImageResource(R.drawable.delete_time_btn)
                background = null
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    (timeSelectionLayout.parent as? ViewGroup)?.removeView(timeSelectionLayout)
                }
                tag = "deleteButton"
            }
            timeSelectionLayout.addView(deleteButton)
        }

        return timeSelectionLayout
    }
}
