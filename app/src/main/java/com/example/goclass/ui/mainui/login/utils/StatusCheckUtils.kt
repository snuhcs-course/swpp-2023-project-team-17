package com.example.goclass.ui.mainui.login.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import com.google.android.material.snackbar.Snackbar

object StatusCheckUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

    fun showNetworkErrorSnackBar(view: View) {
        Snackbar.make(view, "No Network Connection", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#FF515C"))
            .show()
    }

    fun showLoggingInSnackBar(view: View) {
        Snackbar.make(view, "Logging In...", Snackbar.LENGTH_INDEFINITE).show()
    }

    fun showLoginSuccessSnackBar(view: View) {
        Snackbar.make(view, "Login Successful", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#1BBE00"))
            .show()
    }

    fun showLoginFailedSnackBar(view: View) {
        Snackbar.make(view, "Login Failed: Server error", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#FF515C"))
            .show()
    }
}