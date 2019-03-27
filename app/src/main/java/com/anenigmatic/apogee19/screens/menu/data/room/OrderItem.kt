package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "past_order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "order_id") var orderId: Int,
    @ColumnInfo(name = "stall_id") var stallId: Int,
    @ColumnInfo(name = "item_id") var itemId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "quantity") var quantity: Int
)