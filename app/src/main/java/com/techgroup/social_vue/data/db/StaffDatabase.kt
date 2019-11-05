package com.techgroup.social_vue.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techgroup.social_vue.data.dao.StaffDao
import com.techgroup.social_vue.data.model.Staff

@Database(entities = [Staff::class], version = 1)
abstract class StaffDatabase : RoomDatabase() {

    abstract fun staffDao(): StaffDao

    companion object {
        private const val DB_NAME = "staff_db"
        private lateinit var INSTANCE: StaffDatabase

        fun getDatabase(context: Context): StaffDatabase {
            if (!::INSTANCE.isInitialized) {

                synchronized(StaffDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StaffDatabase::class.java, DB_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}