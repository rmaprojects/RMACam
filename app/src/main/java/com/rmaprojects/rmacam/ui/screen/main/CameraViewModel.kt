package com.rmaprojects.rmacam.ui.screen.main

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.rmaprojects.rmacam.data.repository.RMACamRepository
import com.rmaprojects.rmacam.utils.FOLDER_IMAGES
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val application: Application,
    private val repository: RMACamRepository,
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat(
        "yyyy-MM-dd|hh:mm:ss|'rmacam'",
        Locale.getDefault()
    ).format(Date(System.currentTimeMillis()))

    fun savePhotoToDevice(bitmap: Bitmap) {
        val contentResolver = application.contentResolver

        if (File(Environment.getExternalStorageDirectory().path + "/Pictures/.$FOLDER_IMAGES/").mkdirs()) {
            val imageUri = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    val fileName = "$dateFormatter.png"
                    var fos: OutputStream?
                    var imageUri: Uri?
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                        put(MediaStore.Images.Media.RELATIVE_PATH, "$DIRECTORY_PICTURES/.$FOLDER_IMAGES")
                        put(MediaStore.Video.Media.IS_PENDING, 1)
                    }
                    contentResolver.also { resolver ->
                        imageUri =
                            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                        fos = imageUri?.let {
                            resolver.openOutputStream(it)
                        }
                    }
                    fos?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 0, it) }

                    contentValues.clear()
                    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
                    contentResolver.update(imageUri!!, contentValues, null, null)
                    imageUri
                }

                else -> {
                    val file = File(
                        application.applicationContext.getExternalFilesDir("$DIRECTORY_PICTURES/.$FOLDER_IMAGES"),
                        dateFormatter
                    )
                    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 0, it) }
                    MediaScannerConnection.scanFile(
                        application.applicationContext,
                        arrayOf(file.absolutePath),
                        null,
                        null
                    )
                    FileProvider.getUriForFile(
                        application.applicationContext,
                        "${application.applicationContext.packageName}.provider",
                        file
                    )
                }
            }

            if (imageUri != null) {
                repository.uploadToFirebase(imageUri)
            } else {
                Toast.makeText(application, "Gagal menyimpan gambar!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}