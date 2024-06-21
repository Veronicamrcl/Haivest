package com.android.haivest.data.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ImageResult(
    val imageFile: File,
    val imageUri: Uri? = null,
    val isFromCamera: Boolean = true,
    val imageBitmap: Bitmap? = null
) : Parcelable