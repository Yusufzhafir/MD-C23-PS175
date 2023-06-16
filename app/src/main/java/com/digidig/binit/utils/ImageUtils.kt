package com.digidig.binit.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import com.digidig.binit.R
import java.io.*
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun compressImageFile(inputFile: File, outputFile: File, maxSizeInBytes: Long) {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    // Decode the image bounds without actually loading the image into memory
    BitmapFactory.decodeFile(inputFile.absolutePath, options)

    val originalWidth = options.outWidth
    val originalHeight = options.outHeight

    // Calculate the desired width and height based on the maximum size in bytes
    val scaleFactor = Math.sqrt((originalWidth * originalHeight).toDouble() / maxSizeInBytes)

    options.inJustDecodeBounds = false
    options.inSampleSize = scaleFactor.toInt()

    // Decode the image with the calculated sample size
    val compressedBitmap = BitmapFactory.decodeFile(inputFile.absolutePath, options)

    // Compress the bitmap into a ByteArrayOutputStream
    val outputStream = ByteArrayOutputStream()
    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

    // Write the compressed bitmap to the output file
    val fileOutputStream = FileOutputStream(outputFile)
    fileOutputStream.write(outputStream.toByteArray())
    fileOutputStream.flush()
    fileOutputStream.close()
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateFile(file: File, isBackCamera: Boolean = false) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}