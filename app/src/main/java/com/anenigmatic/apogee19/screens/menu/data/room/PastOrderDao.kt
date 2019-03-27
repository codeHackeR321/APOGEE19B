package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

@Dao
interface PastOrderDao {

    @Query("SELECT * FROM past_orders")
    fun getAll(): LiveData<List<PastOrder>>

    @Query("SELECT * FROM past_orders WHERE orderId = :orderId")
    fun getPastOrderItems(orderId: Int): LiveData<List<PastOrder>>

    @Query("SELECT * FROM past_orders WHERE orderId = :orderId")
    fun getPastOrderItemsList(orderId: Int): List<PastOrder>

    @Query("UPDATE past_orders SET show_otp = :bool WHERE orderId = :orderId")
    fun changeOtpSeenStatus(orderId: Int, bool: Boolean)

    @Query("UPDATE past_orders SET status = :status WHERE orderId = :orderId")
    fun changeStatus(orderId: Int, status: String)

    @Insert
    fun insertPastOrders(order: List<PastOrder>)

    @Query("DELETE FROM past_orders")
    fun deleteAll()
}