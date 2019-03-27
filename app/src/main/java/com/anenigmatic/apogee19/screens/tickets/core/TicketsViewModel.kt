package com.anenigmatic.apogee19.screens.tickets.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.shared.util.asMut
import com.anenigmatic.apogee19.screens.tickets.data.TicketRepository
import io.reactivex.schedulers.Schedulers

class TicketsViewModel(private val tRepo: TicketRepository) : ViewModel() {

    sealed class UiOrder {

        object ShowLoadingState : UiOrder()

        data class ShowWorkingState(val tickets: List<Ticket>) : UiOrder()
    }


    val orderData: LiveData<UiOrder> = MutableLiveData()
    val toastData: LiveData<String?> = MutableLiveData()


    init {
        orderData.asMut().value = UiOrder.ShowLoadingState

        tRepo.getPurchaseableTickets()
            .map { tickets -> UiOrder.ShowWorkingState(tickets) }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { order ->
                    orderData.asMut().postValue(order)
                },
                {
                    orderData.asMut().postValue(UiOrder.ShowWorkingState(listOf()))
                    toastData.asMut().postValue("Something went wrong :/")
                }
            )
    }


    @SuppressLint("CheckResult")
    fun onPurchaseTicketAction(ticketId: Long, isCombo: Boolean, quantity: Int) {
        if(quantity <= 0) {
            toastData.asMut().value = "Please enter a positive quantity"
            return
        }

        val backupOrder = orderData.value

        orderData.asMut().postValue(UiOrder.ShowLoadingState)
        tRepo.purchaseTicket(ticketId, isCombo, quantity)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    orderData.asMut().postValue(backupOrder)
                    toastData.asMut().postValue("Purchase Successful")
                },
                {
                    orderData.asMut().postValue(backupOrder)
                    toastData.asMut().postValue("Purchase Failed  :/")
                }
            )
    }
}