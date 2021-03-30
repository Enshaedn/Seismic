package com.enshaedn.seismic.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.enshaedn.seismic.database.Session


@BindingAdapter("sessionItemTitle")
fun TextView.setSessionItemTitle(item: Session) {
    text = item.title
}


@BindingAdapter("sessionItemDate")
fun TextView.setSessionItemDateFormatted(item: Session) {
    text = convertLongToDateOnlyString(item.startTimeMilli)
}