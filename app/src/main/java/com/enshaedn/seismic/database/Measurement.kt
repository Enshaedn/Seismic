package com.enshaedn.seismic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "measurement_table")
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    var measurementID: Long = 0L,

    @ColumnInfo(name = "sessionID")
    val sessionID: Long,

    @ColumnInfo(name = "timestamp_recorded")
    val recordedTimeStamp: Timestamp,

    @ColumnInfo(name = "measurement")
    val measurement: Float
)