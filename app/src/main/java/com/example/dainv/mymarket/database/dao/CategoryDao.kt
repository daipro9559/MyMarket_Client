package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.dainv.mymarket.model.Category

@Dao
interface CategoryDao {
    @Query("Select * FROM Category")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(list:List<Category>)
}