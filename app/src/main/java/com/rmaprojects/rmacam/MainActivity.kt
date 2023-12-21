package com.rmaprojects.rmacam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rmaprojects.rmacam.ui.screen.main.CameraContent
import com.rmaprojects.rmacam.ui.theme.RMACamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERA_X_PERMISSION, 0
            )
        }

        setContent {
            RMACamTheme {
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
                        )
                    }
                }

                controller.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraContent(
                        modifier = Modifier
                            .fillMaxSize(),
                        controller = controller,
                        onOpenGalleryClick = {
                            startActivity(Intent(this, GalleryActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERA_X_PERMISSION.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERA_X_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}