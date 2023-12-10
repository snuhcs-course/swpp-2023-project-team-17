/*
 * StatusCheckUtils is a utility class providing a method to check the network connection status.
 * It determines whether the device is connected to a cellular network, Wi-Fi, or Ethernet.
 */

package com.example.goclass.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object StatusCheckUtils {
    /**
     * Checks if the device is connected to a network (cellular, Wi-Fi, or Ethernet).
     *
     * @param context The application context.
     * @return True if the device is connected to a network, false otherwise.
     */
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}
