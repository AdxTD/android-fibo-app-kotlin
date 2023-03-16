package com.syncvr.fiboapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.syncvr.fiboapp.database.entities.FiboNumber
import com.syncvr.fiboapp.database.entities.FiboRequest

@Database(
    entities = [FiboNumber::class, FiboRequest::class],
    version = 1
)
abstract class FiboDatabase : RoomDatabase(){
    abstract fun getFiboDao() : FiboDao

    companion object{
        @Volatile private var instance : FiboDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FiboDatabase::class.java,
            "fibo_database"
            ).build()
    }
}