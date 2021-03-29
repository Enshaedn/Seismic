package com.enshaedn.seismic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Session::class, Measurement::class],
    version = 2,
    exportSchema = false
)
abstract class SeismicDB : RoomDatabase() {
    abstract val seismicDao: SeismicDao

    companion object {
        @Volatile
        private var INSTANCE: SeismicDB? = null

        fun getInstance(context: Context): SeismicDB {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SeismicDB::class.java,
                        "seismic_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}