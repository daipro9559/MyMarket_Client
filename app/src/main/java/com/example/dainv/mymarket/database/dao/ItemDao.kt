package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.dainv.mymarket.model.Item

@Dao
interface ItemDao {
//    @Query("Select * from Item")
//    fun getItems(): LiveData<List<Item>>
//
//    @Query("Select * From Item")
//    fun getItems(categoryID: Int): LiveData<List<Item>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun saveItems(list: List<Item>)
}
