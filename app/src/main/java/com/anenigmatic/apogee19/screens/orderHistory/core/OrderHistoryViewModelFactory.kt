package com.anenigmatic.apogee19.screens.orderHistory.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.menu.data.MenuRepository
import javax.inject.Inject

class OrderHistoryViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var menuRepository: MenuRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        ApogeeApp.appComponent.newOrdersComponent().inject(this)
        return OrderHistoryViewModel(menuRepository) as T
    }
}