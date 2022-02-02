package com.skillbox.github.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.luminosity.R
import com.example.luminosity.models.DetailUserProfile
import com.example.luminosity.models.ProfileImage
import com.google.android.material.snackbar.Snackbar

const val TEXT_PLAIN = "text/plain"

const val IMAGES_FEED_FRAGMENT = "Images Feed"

const val SEARCH_PHOTO_FRAGMENT = "Search Photo"

const val FAVORITE_PHOTO_FRAGMENT = "Favorite Photo"

const val COLLECTION_PHOTO_FRAGMENT = "Collection photo fragment"

fun Fragment.toast(@StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}

fun haveQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun getEmptyDetailUser(): DetailUserProfile {
    return DetailUserProfile(
        "",
        "",
        "",
        ProfileImage("", "")
    )
}

@SuppressLint("ResourceAsColor")
fun showSnackServerConnectError(view: View, context: Context) {
    try {
        Snackbar.make(
            view,
            context.resources.getString(R.string.server_connection_error),
            Snackbar.LENGTH_LONG
        )
            .setBackgroundTint(R.color.black_main)
            .setAction(R.string.snack_retry) {
                Snackbar.make(view, (R.string.snack_updated), Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setActionTextColor(R.color.red_buttons)
            .setAnchorView(R.id.bottomNavigation)
            .show()
    } catch (t: Throwable){
        Log.d("TOKEN", "Snackbar Error: $t")
    }
}

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}


//fun showSnackSuccessLoad(view: View, context: Context) {
//    try {
//        Snackbar.make(
//            view,
//            context.resources.getString(R.string.image_uploaded_to_gallery),
//            Snackbar.LENGTH_LONG
//        )
//            .setAnchorView(R.id.bottom_navigation_main)
//            .show()
//    } catch (t: Throwable){
//        Log.d("TOKEN", "Snackbar Error: $t")
//    }
//}

//val Context.appComponent: AppComponent
//    get() = when (this){
//        is App -> appComponent
//        else -> this.applicationContext.appComponent
//    }