package com.lolos.asn.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import com.lolos.asn.data.response.NotificationDisplayItem
import com.lolos.asn.data.response.NotificationItem
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
private const val MAXIMAL_SIZE = 1000000

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        @Suppress("DEPRECATION")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        activeNetworkInfo.isConnected
    }
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun groupNotificationsByDate(notifications: List<NotificationItem>): List<NotificationDisplayItem> {
    val groupedItems = mutableListOf<NotificationDisplayItem>()
    val cal = Calendar.getInstance()

    val today = cal.time
    cal.add(Calendar.DATE, -1)
    val yesterday = cal.time

    val todayNotifications = notifications.filter { isSameDay(it.createdAt, today) }
    val yesterdayNotifications = notifications.filter { isSameDay(it.createdAt, yesterday) }
    val otherNotifications = notifications.filter { !isSameDay(it.createdAt, today) && !isSameDay(it.createdAt, yesterday) }

    if (todayNotifications.isNotEmpty()) {
        groupedItems.add(NotificationDisplayItem.Header("Baru"))
        groupedItems.addAll(todayNotifications.map { NotificationDisplayItem.DisplayItem(it) })
    }

    if (yesterdayNotifications.isNotEmpty()) {
        groupedItems.add(NotificationDisplayItem.Header("Kemaren"))
        groupedItems.addAll(yesterdayNotifications.map { NotificationDisplayItem.DisplayItem(it) })
    }

    if (otherNotifications.isNotEmpty()) {
        otherNotifications.groupBy { SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(parseDate(it.createdAt)) }
            .forEach { (date, notifications) ->
                groupedItems.add(NotificationDisplayItem.Header(date))
                groupedItems.addAll(notifications.map { NotificationDisplayItem.DisplayItem(it) })
            }
    }

    return groupedItems
}

private fun isSameDay(dateStr: String, date: Date): Boolean {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val parsedDate = inputFormat.parse(dateStr) ?: return false

    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = parsedDate
    cal2.time = date
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun parseDate(dateStr: String): Date? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    return inputFormat.parse(dateStr)
}

@SuppressLint("DefaultLocale")
fun formatSecondsToMinutesSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}
