package com.anenigmatic.apogee19.screens.menu.data.room


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "stall_items")
data class StallItem (

    @PrimaryKey @field:Json(name = "id")var itemId: Int,
    @ColumnInfo(name = "stall_id") @field:Json(name = "vendor_id") var stallId: Int,
    @ColumnInfo(name = "name") @field:Json(name = "name") var name: String?,
    @ColumnInfo(name = "price") @field:Json(name = "price") var price: Int,
    @ColumnInfo(name = "is_available") @field:Json(name = "is_available") var isAvailable: Boolean

)