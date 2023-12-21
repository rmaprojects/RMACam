package com.rmaprojects.rmacam.data.worker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.rmaprojects.rmacam.utils.KEY_IMAGE_URI
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class UploadFileWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val dateFormatter = SimpleDateFormat(
        "yyyy-MM-dd|hh:mm:ss|'rmacam'",
        Locale.getDefault()
    ).format(Date(System.currentTimeMillis()))

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            delay(2000)
            return@withContext try {
                val imageUrl = inputData.getString(KEY_IMAGE_URI)

                if (!imageUrl.isNullOrEmpty()) {
                    Firebase.storage.getReference("photo")
                        .child("$dateFormatter.png")
                        .putFile(imageUrl.toUri())
                        .await()
                    Result.success()
                } else {
                    Log.e(
                        "ERROR_WHEN_UPLOADING_FILE",
                        "Writing Data failed"
                    )
                    Result.failure()
                }
            } catch (e: Exception) {
                Log.e(
                    "ERROR_WHEN_UPLOADING_FILE",
                    "ERROR_WHEN_UPLOADING_FILE",
                    e
                )
                Result.failure()
            }
        }
    }
}