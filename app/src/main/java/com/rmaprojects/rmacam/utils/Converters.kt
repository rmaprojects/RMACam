package com.rmaprojects.rmacam.utils

import android.net.Uri
import androidx.work.Data

fun createDataFromUri(uri: Uri): Data {
    val builder = Data.Builder()
    uri.let {
        builder.putString(KEY_IMAGE_URI, uri.toString())
    }
    return builder.build()
}