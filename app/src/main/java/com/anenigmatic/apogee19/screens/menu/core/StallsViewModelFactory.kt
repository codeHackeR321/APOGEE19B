package com.anenigmatic.apogee19.screens.menu.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.menu.data.MenuRepository
import javax.inject.Inject

class StallsViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var menuRepository: MenuRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        ApogeeApp.appComponent.newMenuComponent().inject(this)
        return StallsViewModel(menuRepository) as T
    }
}