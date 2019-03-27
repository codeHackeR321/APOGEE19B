package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

@Dao
interface OrderItemDao {

    @Query("SELECT * FROM past_order_items")
    fun getAll(): LiveData<List<OrderItem>>

    @Query("SELECT * FROM past_order_items")
    fun getPastOrders(): List<OrderItem>

    @Query("SELECT * FROM past_order_items WHERE order_id = :orderId")
    fun getPastOrderItemsList(orderId: Int): LiveData<List<OrderItem>>

    @Insert
    fun insertOrderItem(orderItem: List<OrderItem>)

    @Delete
    fun delete(orderItem: OrderItem)

    @Query("DELETE FROM past_order_items")
    fun deleteAll()

}