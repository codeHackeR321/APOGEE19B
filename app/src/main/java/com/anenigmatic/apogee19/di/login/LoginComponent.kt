package com.anenigmatic.apogee19.di.login

import com.anenigmatic.apogee19.screens.login.core.ChooseAvatarViewModelFactory
import com.anenigmatic.apogee19.screens.login.core.ChooseLoginViewModelFactory
import com.anenigmatic.apogee19.screens.login.core.OutsteeLoginViewModelFactory
import dagger.Subcomponent

@Subcomponent
interface LoginComponent {

    fun inject(factory: ChooseLoginViewModelFactory)

    fun inject(factory: OutsteeLoginViewModelFactory)

    fun inject(factory: ChooseAvatarViewModelFactory)
}