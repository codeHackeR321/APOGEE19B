package com.anenigmatic.apogee19.screens.tickets.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.tickets.data.TicketRepository
import javax.inject.Inject

class TicketsViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var ticketRepository: TicketRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        ApogeeApp.appComponent.newTicketsComponent().inject(this)
        return TicketsViewModel(ticketRepository) as T
    }
}