package io.devexpert.splitbill.ui.screens.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import io.devexpert.splitbill.R
import java.io.File


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onTicketProcessed: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // val isButtonEnabled = uiState.scansLeft > 0

    // Initialize or reset if needed when loading the screen
    LaunchedEffect(uiState.tickedProcessed) {
        if (uiState.tickedProcessed) {
            onTicketProcessed()
            viewModel.resetTicketProcessed()
        }
    }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    fun resizeBitmapToMaxWidth(bitmap: Bitmap, maxWidth: Int): Bitmap {
        if (bitmap.width <= maxWidth) return bitmap
        val aspectRatio = bitmap.height.toFloat() / bitmap.width
        val newWidth = maxWidth
        val newHeight = (maxWidth * aspectRatio).toInt()
        return bitmap.scale(newWidth, newHeight)
    }


    // launcher to take a picture with the camera (high resolution)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && photoUri != null) {
            val inputStream = context.contentResolver.openInputStream(photoUri!!)
            val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }
            if (bitmap != null) {
                viewModel.processTicket(bitmap)
            }
            /*
             // resize before processing
             val resizedBitmap = resizeBitmapToMaxWidth(bitmap, 1280)
             capturedPhoto = resizedBitmap
             isProcessing = true
             errorMessage = null
             // Process image with AI in a coroutine
             coroutineScope.launch {
                 try {
                     viewModel.processTicket(bitmap)
                     isProcessing = false
                     // call the callback to navigate to the next screen
                     onTicketProcessed()
                 } catch (error: Exception) {

                     errorMessage = context.getString(
                         R.string.error_processing_ticket,
                         error.message ?: ""
                     )
                     isProcessing = false
                 }
             }
         }*/
        }
    }


    Scaffold { padding ->
        Box(
            modifier = modifier
                .padding(paddingValues = padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (uiState.scansLeft > 0)
                        stringResource(id = R.string.scans_remaining, uiState.scansLeft)
                    else
                        stringResource(id = R.string.no_scans_remaining),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Button(
                    onClick = {
                        // create a temporary file for the photo
                        val photoFile = File.createTempFile("ticket_", ".jpg", context.cacheDir)
                        val uri = FileProvider.getUriForFile(
                            context,
                            "io.devexpert.splitbill.fileprovider",
                            photoFile
                        )
                        photoUri = uri
                        cameraLauncher.launch(uri)

                    },
                    enabled = uiState.scansLeft > 0 && !uiState.isLoading,
                    modifier = Modifier.size(width = 320.dp, height = 64.dp),
                    shape = ButtonDefaults.shape
                ) {
                    Text(
                        text = if (uiState.isLoading)
                            stringResource(id = R.string.processing)
                        else
                            stringResource(id = R.string.scan_ticket),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // show result of the processing
                when {
                    uiState.isLoading -> {
                        Text(
                            text = stringResource(R.string.photo_captured_processing),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage!!,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
} 