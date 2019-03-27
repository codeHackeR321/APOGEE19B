package com.anenigmatic.apogee19.screens.menu.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartItemDao {

    @Query("SELECT * FROM cart_items")
    fun getAll(): LiveData<List<CartItem>>

    @Query("SELECT * FROM cart_items")
    fun getCart(): List<CartItem>

    @Insert
    fun insertCartItem(cartItem: CartItem)

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE itemId = :itemId")
    fun changeQuantity(itemId: Int, newQuantity: Int)

    @Delete
    fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_items")
    fun deleteAll()

}
