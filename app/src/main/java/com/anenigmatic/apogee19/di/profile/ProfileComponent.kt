package com.anenigmatic.apogee19.di.profile

import com.anenigmatic.apogee19.screens.profile.core.ProfileViewModelFactory
import dagger.Subcomponent

@Subcomponent
interface ProfileComponent {

    fun inject(factory: ProfileViewModelFactory)
}