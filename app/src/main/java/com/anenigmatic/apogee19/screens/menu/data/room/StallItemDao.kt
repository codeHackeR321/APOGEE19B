package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StallItemDao {
    @Query("SELECT * FROM stall_items")
    fun getAll(): LiveData<List<StallItem>>

    @Query("SELECT * FROM stall_items WHERE stall_id = :stallId")
    fun getStallItems(stallId: Int): LiveData<List<StallItem>>

    @Query("SELECT * FROM stall_items WHERE stall_id = :stallId")
    fun getStallItemsList(stallId: Int): List<StallItem>

    @Insert
    fun insertAll(stallItems: List<StallItem>)

    @Delete
    fun delete(stallItem: StallItem)

    @Query("DELETE FROM stall_items")
    fun deleteAll()
}
