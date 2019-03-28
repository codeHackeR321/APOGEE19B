package com.anenigmatic.apogee19.screens.orderHistory.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.menu.data.MenuRepository
import com.anenigmatic.apogee19.screens.menu.data.room.OrderItem
import com.anenigmatic.apogee19.screens.menu.data.room.Stall
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder

class OrderHistoryViewModel(private val repository: MenuRepository): ViewModel(){

    var orderList: LiveData<List<PastOrder>> = repository.getOrders()
    var orderItemList: LiveData<List<OrderItem>> = MediatorLiveData()
    var otpStatus = repository.showOtpRequestStatus

    fun getOrderListFromServer(){
        repository.refreshPastOrders()
    }

    fun getOrderListForOrder(orderId: Int){
        orderItemList = repository.getOrderItems(orderId)
    }

    fun getStallListFromServer(): LiveData<List<Stall>>
    {
        return repository.getStalls()
    }


    fun onOTPClicked(orderId: Int){

        repository.changeOrderOtpStatus(orderId)

    }
}
