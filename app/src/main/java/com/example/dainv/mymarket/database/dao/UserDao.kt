package com.example.dainv.mymarket.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.User

//@Dao
//interface UserDao {
//    @Query("Select * From User Where userID = :userID")
//    fun getProfile(userID:String): User
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun save(user: User)
//    @Delete
//    fun deleteUser(user:User)
//}