package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.dainv.mymarket.model.District

@Dao
interface DistrictDao {
    @Query("Select * FROM District Where provinceID =:provinceID")
    fun getAllDistrict(provinceID:Int):LiveData<List<District>>
}