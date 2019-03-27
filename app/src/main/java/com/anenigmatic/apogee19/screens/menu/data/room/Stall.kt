package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "stall")
data class Stall (

    @PrimaryKey @field:Json(name = "id")var stallId: Int,
    @ColumnInfo(name = "name") @field:Json(name = "name")var name: String?,
    @ColumnInfo(name = "description") @field:Json(name = "description") var description: String?,
    @ColumnInfo(name = "closed") @field:Json(name = "closed")var isClosed: Boolean
    //@Ignore @SerializedName("menu") var menu: List<StallItem>

)