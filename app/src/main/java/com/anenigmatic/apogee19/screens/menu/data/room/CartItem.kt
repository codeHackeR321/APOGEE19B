package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "cart_items")
data class CartItem(

    @PrimaryKey @field:Json(name = "item_id")var itemId: Int,
    @ColumnInfo(name = "stall_id") var stallId: Int,
    @ColumnInfo(name = "name") @field:Json(name = "name") var name: String?,
    @ColumnInfo(name = "price") @field:Json(name = "price") var price: Int,
    @ColumnInfo(name = "quantity") @field:Json(name = "quantity") var quantity: Int

)