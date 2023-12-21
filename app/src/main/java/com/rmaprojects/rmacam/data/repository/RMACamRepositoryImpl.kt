package com.rmaprojects.rmacam.data.repository

import android.app.Application
import android.net.Uri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rmaprojects.rmacam.data.worker.UploadFileWorker
import com.rmaprojects.rmacam.utils.KEY_IMAGE_URI
import com.rmaprojects.rmacam.utils.createDataFromUri
import javax.inject.Inject

class RMACamRepositoryImpl @Inject constructor(
    application: Application
) : RMACamRepository {

    private val workManager = WorkManager.getInstance(application.applicationContext)

    override fun uploadToFirebase(
        fileUri: Uri
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
        val uploaderBuilder = OneTimeWorkRequestBuilder<UploadFileWorker>()
        uploaderBuilder.setInputData(createDataFromUri(fileUri))
        uploaderBuilder.setConstraints(constraints.build())

        workManager.enqueueUniqueWork(
            fileUri.pathSegments.last(),
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            uploaderBuilder.build()
        )
    }
}