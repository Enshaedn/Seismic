package com.enshaedn.seismic.utils

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

class SessionItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)