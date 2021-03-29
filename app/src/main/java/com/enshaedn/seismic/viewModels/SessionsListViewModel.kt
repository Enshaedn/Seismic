package com.enshaedn.seismic.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class SessionsListViewModel(val database: SeismicDao, application: Application
) : AndroidViewModel(application)

{
    private val TAG = "SEISMIC_LOG"
    val sessions = database.getAllSessions()

    val sessionsString = Transformations.map(sessions) { sessions ->
//        formatSessions(sessions, application.resources)
    }

//    private fun formatSessions(sessions: List<Session>, resources: Resources): Spanned {
//        val sb = StringBuilder()
//        sb.apply {
//            append(resources.getString(R.string.title))
//            sessions.forEach {
//                append("<br>")
//                append(it.title)
//                append("<br>")
//                append(resources.getString(R.string.start_time))
//                append("\t${convertLongToDateString(it.startTimeMilli)}<br>")
//                if (it.endTimeMilli != it.startTimeMilli) {
//                    append(resources.getString(R.string.end_time))
//                    append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
//                    append(resources.getString(R.string.hours_slept))
//                    // Hours
//                    append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
//                    // Minutes
//                    append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
//                    // Seconds
//                    append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")
//                }
//                append(it.note)
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
//        } else {
//            return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
//        }
//    }

    val clearButtonVisible = Transformations.map(sessions) {
        it?.isNotEmpty()
    }

    fun onClear() {
        Log.d(TAG, "Clear Table")
        viewModelScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        database.clearSessions()
    }
}