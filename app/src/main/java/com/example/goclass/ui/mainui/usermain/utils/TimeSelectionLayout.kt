/*
 * TimeSelectionLayout is a utility object that creates a time selection layout dynamically.
 */

package com.example.goclass.ui.mainui.usermain.utils

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.goclass.R

object TimeSelectionLayout {

    /*
     * create creates a time selection layout dynamically.
     *
     * @param context: Context, the context used to create the layout.
     * @param deleteButtonRequired: Boolean, flag indicating whether the delete button should be visible.
     * @return ConstraintLayout, the dynamically created time selection layout.
     */
    fun create(
        context: Context,
        deleteButtonRequired: Boolean = true
    ): ConstraintLayout {
        val inflater = LayoutInflater.from(context)
        val timeSelectionLayout = inflater.inflate(R.layout.time_selection_layout, null) as ConstraintLayout

        val dayDropdown = timeSelectionLayout.findViewById<Spinner>(R.id.dayDropdown)
        val startTimeButton = timeSelectionLayout.findViewById<AppCompatButton>(R.id.startTimeButton)
        val endTimeButton = timeSelectionLayout.findViewById<AppCompatButton>(R.id.endTimeButton)
        val deleteButton = timeSelectionLayout.findViewById<ImageButton>(R.id.deleteButton)

        // day dropdown
        val daysOfWeek = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri")
        val dayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            daysOfWeek
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        dayDropdown.adapter = dayAdapter

        val timePickerListener = { buttonToUpdate: Button ->
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                buttonToUpdate.text = String.format("%02d:%02d", hourOfDay, minute)
            }
        }

        // start time button
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

        // end time button
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

        deleteButton.visibility = if (deleteButtonRequired) View.VISIBLE else View.GONE
        deleteButton.setOnClickListener {
            // Remove the time selection layout from its parent
            (timeSelectionLayout.parent as? ViewGroup)?.removeView(timeSelectionLayout)
        }

        return timeSelectionLayout
    }
}
