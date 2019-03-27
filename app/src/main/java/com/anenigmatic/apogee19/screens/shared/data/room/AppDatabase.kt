package com.anenigmatic.apogee19.screens.shared.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anenigmatic.apogee19.screens.events.core.Event
import com.anenigmatic.apogee19.screens.events.data.room.EventsDao
import com.anenigmatic.apogee19.screens.menu.data.room.*
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

@Database(entities = [Event::class, Stall::class,StallItem::class,CartItem::class, PastOrder::class,OrderItem::class], version = 1, exportSchema = true)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getEventsDao(): EventsDao

    abstract fun stallDao(): StallDao

    abstract fun stallItemDao(): StallItemDao

    abstract fun cartItemDao(): CartItemDao

    abstract fun pastOrderDao(): PastOrderDao

    abstract fun pastOrderItemsDao(): OrderItemDao
}