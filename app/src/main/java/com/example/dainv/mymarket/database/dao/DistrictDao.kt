package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.dainv.mymarket.entity.District

@Dao
interface DistrictDao {
    @Query("Select * FROM District Where provinceID =:provinceID")
    fun getAllDistrict(provinceID: Int): LiveData<List<District>>

    @Query("Select * FROM District Where provinceID =:districtID")
    fun geDistrict(districtID: Int): LiveData<District>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(listDistrict: List<District>)
}