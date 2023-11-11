package com.example.goclass.ui.mainui.profile.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarUtils {
    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#FF515C"))
            .show()
    }
}