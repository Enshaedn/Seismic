package com.enshaedn.seismic.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SeismicDao {
    @Insert
    fun insertSession(session: Session)

    @Update
    fun updateSession(session: Session)

    @Delete
    fun deleteSession(session: Session)

    @Query("SELECT * FROM session_table WHERE sessionID = :key")
    fun get(key: Long): Session?

    @Query("DELETE FROM session_table")
    fun clear()

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC LIMIT 1")
    fun getLastSession(): Session?

    @Query("SELECT * FROM session_table ORDER BY sessionID DESC")
    fun getAllSessions(): LiveData<List<Session>>
}