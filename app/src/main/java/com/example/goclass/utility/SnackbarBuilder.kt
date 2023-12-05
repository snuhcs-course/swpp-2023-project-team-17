package com.example.goclass.utility

import android.view.View
import androidx.core.content.ContextCompat
import com.example.goclass.R
import com.google.android.material.snackbar.Snackbar

class SnackbarBuilder(private val view: View) {
    private var message: String = ""
    private var duration: Int = Snackbar.LENGTH_SHORT
    private var backgroundColor: Int = R.color.black

    fun setMessage(message: String): SnackbarBuilder {
        this.message = message
        return this
    }

    fun setDuration(duration: Int): SnackbarBuilder {
        this.duration = duration
        return this
    }

    fun setBackgroundColor(color: Int): SnackbarBuilder {
        this.backgroundColor = color
        return this
    }

    fun build(): Snackbar {
        val snackbar = Snackbar.make(view, message, duration)
        val context = view.context
        val actualColor = ContextCompat.getColor(context, backgroundColor)
        snackbar.setBackgroundTint(actualColor)
        return snackbar
    }
}