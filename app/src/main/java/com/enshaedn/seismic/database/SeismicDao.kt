package com.enshaedn.seismic.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SeismicDao {
    @Insert
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session_table WHERE sessionID = :key")
    suspend fun get(key: Long): Session?

    @Query("DELETE FROM session_table")
    suspend fun clearSessions()

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC LIMIT 1")
    suspend fun getLastSession(): Session?

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC")
    fun getAllSessions(): LiveData<List<Session>>
}