package com.enshaedn.seismic.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SeismicDao {
    //Session Queries
    @Insert
    suspend fun insert(session: Session)

    @Update
    suspend fun update(session: Session)

    @Delete
    suspend fun delete(session: Session)

    @Query("SELECT * FROM session_table WHERE sessionID = :key")
    suspend fun get(key: Long): Session?

    @Query("DELETE FROM session_table")
    suspend fun clearSessions()

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC LIMIT 1")
    suspend fun getCurrentSession(): Session?

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC")
    fun getAllSessions(): LiveData<List<Session>>

    //Measurement Queries
    @Insert
    suspend fun insert(vararg measurement: Measurement)

    @Query("SELECT * FROM measurement_table ORDER BY measurementID DESC")
    fun getAllMeasurements(): LiveData<List<Measurement>>

    //SessionMeasurements Query
    @Transaction
    @Query("SELECT * FROM session_table WHERE sessionID = :key")
    suspend fun getSessionMeasurements(key: Long): List<SessionMeasurements>
}