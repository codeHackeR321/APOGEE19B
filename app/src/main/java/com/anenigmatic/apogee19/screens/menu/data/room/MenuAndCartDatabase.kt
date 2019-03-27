package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

@Database(entities = arrayOf(Stall::class,StallItem::class,CartItem::class,PastOrder::class,OrderItem::class), version = 1)
abstract class StallsAndMenuDataBase : RoomDatabase() {
    abstract fun stallDao(): StallDao

    abstract fun stallItemDao(): StallItemDao

    abstract fun cartItemDao(): CartItemDao

    abstract fun pastOrderDao(): PastOrderDao

    abstract fun pastOrderItemsDao(): OrderItemDao
}