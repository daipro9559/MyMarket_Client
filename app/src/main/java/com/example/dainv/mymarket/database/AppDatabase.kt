package com.example.dainv.mymarket.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.dainv.mymarket.model.User

@Database(entities = [User::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

}