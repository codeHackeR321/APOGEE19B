package com.anenigmatic.apogee19.di.orders

import com.anenigmatic.apogee19.screens.orderHistory.core.OrderHistoryViewModelFactory
import dagger.Subcomponent

@Subcomponent
interface OrdersComponent {

    fun inject(factory: OrderHistoryViewModelFactory)
}