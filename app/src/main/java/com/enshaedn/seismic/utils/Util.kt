package com.enshaedn.seismic.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm:ss:SSS")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateOnlyString(systemTime: Long): String {
    return SimpleDateFormat("MM/dd/yy")
        .format(systemTime).toString()
}

@Suppress("SimpleDateFormat")
fun convertLongToDate(systemTime: Long): Date {
    val date = convertLongToDateString(systemTime)
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm:ss:SSS").parse(date)!!
}