package com.example.dainv.mymarket.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.dainv.mymarket.database.dao.CategoryDao
import com.example.dainv.mymarket.database.dao.DistrictDao
import com.example.dainv.mymarket.database.dao.ProvinceDao
//import com.example.dainv.mymarket.database.dao.UserDao
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.District
import com.example.dainv.mymarket.entity.Province

@Database(entities = [Category::class,District::class,Province::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun districtDao(): DistrictDao
    abstract fun provinceDao() : ProvinceDao
//    abstract fun userDao():UserDao
}