package com.rmaprojects.rmacam

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rmaprojects.rmacam.ui.screen.main.CameraViewModel
import com.rmaprojects.rmacam.utils.FOLDER_IMAGES
import java.io.File


class GalleryActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val folder = File(Environment.getExternalStorageDirectory().toString() + "/Pictures/" + "." + FOLDER_IMAGES)
        val allFiles = folder.listFiles { _, name ->
            name.endsWith(
                "rmacam.png"
            )
        }

        setContent {

            BackHandler {
                finish()
            }

            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(text = "Galeri")
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIos,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        if (allFiles.isNullOrEmpty()) {
                            Text(text = "Belum ada foto yang diambil")
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                items(allFiles) { file ->
                                    Card(
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                        shape = RoundedCornerShape(24.dp)
                                    ) {
                                        AsyncImage(model = file, contentDescription = file.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}