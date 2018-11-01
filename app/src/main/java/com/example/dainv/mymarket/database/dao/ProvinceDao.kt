package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.dainv.mymarket.model.Province

@Dao
interface ProvinceDao {
    @Query("Select * from Province")
    fun getAll():LiveData<List<Province>>
}