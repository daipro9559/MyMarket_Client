package com.example.dainv.mymarket.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.dainv.mymarket.database.dao.CategoryDao
import com.example.dainv.mymarket.database.dao.DistrictDao
import com.example.dainv.mymarket.database.dao.ProvinceDao
import com.example.dainv.mymarket.database.dao.UserDao
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.model.User

@Database(entities = [Category::class,District::class,Province::class,User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun districtDao(): DistrictDao
    abstract fun provinceDao() : ProvinceDao
    abstract fun userDao():UserDao
}