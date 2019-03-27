package com.anenigmatic.apogee19.di.menu

import com.anenigmatic.apogee19.screens.menu.core.CartViewModelFactory
import com.anenigmatic.apogee19.screens.menu.core.StallsViewModelFactory
import dagger.Subcomponent

@Subcomponent
interface MenuComponent {

    fun inject(factory: CartViewModelFactory)

    fun inject(factory: StallsViewModelFactory)
}