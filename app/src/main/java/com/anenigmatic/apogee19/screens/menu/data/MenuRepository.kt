package com.anenigmatic.apogee19.screens.menu.data

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.FtsOptions
import com.anenigmatic.apogee19.screens.menu.data.retrofit.Order
import com.anenigmatic.apogee19.screens.menu.data.room.CartItem
import com.anenigmatic.apogee19.screens.menu.data.room.OrderItem
import com.anenigmatic.apogee19.screens.menu.data.room.Stall
import com.anenigmatic.apogee19.screens.menu.data.room.StallItem
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

interface MenuRepository {

    val placeOrderStatus: LiveData<Boolean>
    val showOtpRequestStatus: LiveData<Boolean>
    var toastMessage : LiveData<String>



    fun getStalls(): LiveData<List<Stall>>

    fun getMenu(stall: Stall) : LiveData<List<StallItem>>

    fun getMenu(stallId: Int) : LiveData<List<StallItem>>

    fun refreshStallAndMenu()

    fun addItemToCart(stallItem: StallItem, quantity: Int)

    fun addItemToCart(newCartItem: CartItem)

    fun getCartItems(): LiveData<List<CartItem>>

    fun placeOrder()

    fun deleteCartItem(item : CartItem)

    fun getOrders(): LiveData<List<PastOrder>>

    fun getOrderItems(orderId: Int): LiveData<List<OrderItem>>

    fun changeOrderOtpStatus(orderId: Int)

    fun changeOrderStatus(orderId: Int, status: String)

    fun refreshPastOrders()

    fun listenStatus()

    fun listenToastMessage() : LiveData<String>

}