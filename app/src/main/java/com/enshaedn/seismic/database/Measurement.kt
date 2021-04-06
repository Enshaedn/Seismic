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

    @ColumnInfo(name = "sessionID", index = true)
    val sessionID: Long,

    @ColumnInfo(name = "timestamp_recorded")
    val recorded: Long,

    @ColumnInfo(name = "x_value")
    val xValue: Float,

    @ColumnInfo(name = "y_value")
    val yValue: Float,

    @ColumnInfo(name = "z_value")
    val zValue: Float
)

data class SessionMeasurements(
    @Embedded
    val session: Session,
    @Relation(
        parentColumn = "sessionID",
        entityColumn = "sessionID"
    )
    val sessionMeasurements: List<Measurement>
)