package com.enshaedn.seismic.database

import androidx.room.*
import java.sql.Timestamp

@Entity(tableName = "measurement_table",
    foreignKeys = arrayOf(ForeignKey(entity = Session::class,
        parentColumns = arrayOf("sessionID"),
        childColumns = arrayOf("sessionID"),
        onDelete = ForeignKey.CASCADE)))
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

data class SessionMeasurements(
    @Embedded
    @Relation(
        parentColumn = "sessionID",
        entityColumn = "sessionID"
    )
    val sessionMeasurements: List<Measurement>
)