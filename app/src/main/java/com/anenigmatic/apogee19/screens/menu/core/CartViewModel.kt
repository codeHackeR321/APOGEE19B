package com.anenigmatic.apogee19.screens.menu.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.menu.data.MenuRepository
import com.anenigmatic.apogee19.screens.menu.data.MenuRepositoryImpl
import com.anenigmatic.apogee19.screens.menu.data.room.CartItem
import com.anenigmatic.apogee19.screens.menu.view.CartDialog
import com.anenigmatic.apogee19.screens.shared.util.SingleLiveEvent

class CartViewModel(private val repository: MenuRepository) : ViewModel()
{
    var cartList : LiveData<List<CartItem>> = MutableLiveData<List<CartItem>>()
    var message : LiveData<String> = SingleLiveEvent<String>()

    var placeOrderStatus: LiveData<Boolean> = repository.placeOrderStatus

    init {
        message = repository.toastMessage
    }

    fun getCartItems()
    {
        cartList = repository.getCartItems()
    }

    fun removeItemFromCart(item : CartItem)
    {
        repository.deleteCartItem(item)
        repository.getCartItems()
    }

    fun placeOrder()
    {
        repository.placeOrder()
    }

}