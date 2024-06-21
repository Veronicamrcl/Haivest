package com.android.haivest.data.utils

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.android.haivest.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createFile(application: Application): File {
    val mediasDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    val outputsDirectory = if (
        mediasDir != null && mediasDir.exists()
    ) mediasDir else application.filesDir
    return File(outputsDirectory, "$timeStamp.jpg")
}

fun createCustomTempFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun Uri.toFile(context: Context): File {
    val contentResolvers = context.contentResolver
    val myFiles = createCustomTempFile(context)
    val inputStream = contentResolvers.openInputStream(this) as InputStream
    val outputStream = FileOutputStream(myFiles)
    val buff = ByteArray(1024)
    var len: Int
    while (inputStream.read(buff).also { len = it } > 0) outputStream.write(buff, 0, len)
    outputStream.close()
    inputStream.close()
    return myFiles
}

fun File.toBitmap(): Bitmap {
    return BitmapFactory.decodeFile(this.path)
}

fun Bitmap.rotateBitmap(isBackCamera: Boolean = false): Bitmap {
    val matrixes = Matrix()
    return if (isBackCamera) {
        matrixes.postRotate(90f)
        Bitmap.createBitmap(
            this,
            0,
            0,
            this.width,
            this.height,
            matrixes,
            true
        )
    } else {
        matrixes.postRotate(-90f)
        matrixes.postScale(-1f, 1f, this.width / 2f, this.height / 2f)
        Bitmap.createBitmap(
            this,
            0,
            0,
            this.width,
            this.height,
            matrixes,
            true
        )
    }
}
