package com.example.manish.apogeewallet.screens.menu.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "past_orders")
data class PastOrder(
    @PrimaryKey var orderId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "otp") var otp: Int,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "show_otp") var showOtp: Boolean
    )