package com.example.finalahorasi.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.view.PreviewView
import androidx.lifecycle.lifecycleScope
import com.example.finalahorasi.retrofit.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCaptured: (Bitmap) -> Unit,
    executor: ExecutorService,
    imageCapture: ImageCapture // Ahora pasamos una instancia de ImageCapture
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            startCamera(ctx, previewView, imageCapture)
            previewView
        }
    )
}

private fun startCamera(
    context: Context,
    previewView: PreviewView,
    imageCapture: ImageCapture
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as androidx.lifecycle.LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture // Se pasa imageCapture para tomar fotos
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Error al inicializar la cámara", e)
        }
    }, ContextCompat.getMainExecutor(context))
}

// Función para capturar la foto
fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: ExecutorService,
    onImageCaptured: (Bitmap) -> Unit
) {
    val file = File(context.cacheDir, "captured_image.jpg")

    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                onImageCaptured(bitmap)
                Log.d("Camera", "Imagen guardada en: ${file.absolutePath}")

                // Llamamos a la función para subir la imagen a la API
                uploadImage(context, file)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Error al capturar la foto: ${exception.message}", exception)
            }
        }
    )
}

// Función para subir la imagen a la API
// Función para subir la imagen a la API
private fun uploadImage(context: Context, imageFile: File) {
    val apiService = Client.instance

    if (!imageFile.exists()) {
        Log.e("Upload", "El archivo de imagen no existe")
        return
    }

    (context as? androidx.lifecycle.LifecycleOwner)?.lifecycleScope?.launch {
        try {
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
            val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)

            val response = withContext(Dispatchers.IO) {
                apiService.uploadImage(imagePart)
            }

            if (response.isSuccessful) {
                Log.d("Upload", "Imagen subida exitosamente: ${response.body()?.string()}")
            } else {
                Log.e("Upload", "Error en la subida: ${response.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            Log.e("Upload", "Error en la red: ${e.message}", e)
        }
    }
}