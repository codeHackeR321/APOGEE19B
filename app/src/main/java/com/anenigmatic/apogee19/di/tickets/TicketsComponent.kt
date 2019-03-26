package com.anenigmatic.apogee19.di.tickets

import com.anenigmatic.apogee19.screens.tickets.core.TicketsViewModelFactory
import dagger.Subcomponent

@Subcomponent
interface TicketsComponent {

    fun inject(factory: TicketsViewModelFactory)
}