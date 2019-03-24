package com.anenigmatic.apogee19.screens.profile.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import javax.inject.Inject

class ProfileViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var userRepository: UserRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        ApogeeApp.appComponent.newProfileComponent().inject(this)
        return ProfileViewModel(userRepository) as T
    }
}