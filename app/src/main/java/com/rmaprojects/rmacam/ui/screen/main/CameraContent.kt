package com.rmaprojects.rmacam.ui.screen.main

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.rmaprojects.rmacam.ui.screen.main.CameraPreview

@Composable
fun CameraContent(
    modifier: Modifier = Modifier,
    onOpenGalleryClick: () -> Unit,
    controller: LifecycleCameraController,
    viewModel: CameraViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(vertical = 24.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onOpenGalleryClick
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.background,
                        imageVector = Icons.Default.Image,
                        contentDescription = "Open Gallery"
                    )
                }
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        onTakePhoto(
                            context,
                            controller,
                            onPhotoTaken = viewModel::savePhotoToDevice
                        )
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.background,
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Take Picture"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CameraPreview(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                controller = controller
            )
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Change Camera Direction"
                )
            }
        }
    }
}

private fun onTakePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image.toBitmap())
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("ERROR_WHEN_CAPTURING_PHOTO", exception.toString())
            }
        }
    )
}