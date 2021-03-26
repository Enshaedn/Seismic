package com.enshaedn.seismic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session (
    @PrimaryKey(autoGenerate = true)
    var sessionID: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    val endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "session_note")
    var note: String = "",

    @ColumnInfo(name = "session_title")
    var title: String = ""
)