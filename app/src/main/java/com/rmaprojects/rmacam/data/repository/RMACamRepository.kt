package com.rmaprojects.rmacam.data.repository

import android.net.Uri

interface RMACamRepository {
    fun uploadToFirebase(
        fileUri: Uri
    )
}