package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.dainv.mymarket.model.Province

@Dao
interface ProvinceDao {
    @Query("Select * from Province")
    fun getAll(): LiveData<List<Province>>

    @Query("Select * from Province Where provinceID =:id")
    fun getProvince(id:Int): LiveData<Province>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(listProvince: List<Province>)
}