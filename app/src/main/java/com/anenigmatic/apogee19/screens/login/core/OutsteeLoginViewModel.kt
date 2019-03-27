package com.anenigmatic.apogee19.screens.login.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.util.SingleLiveEvent
import com.anenigmatic.apogee19.screens.shared.util.asMut
import com.anenigmatic.apogee19.screens.shared.util.extractMessage
import io.reactivex.schedulers.Schedulers

class OutsteeLoginViewModel(private val uRepo: UserRepository) : ViewModel() {

    sealed class UiOrder {

        object ShowLoadingState : UiOrder()

        object ShowWorkingState : UiOrder()

        object GoToChooseAvatar : UiOrder()
    }


    val orderData: LiveData<UiOrder> = MutableLiveData()
    val toastData: LiveData<String?> = SingleLiveEvent()


    init {
        orderData.asMut().value = UiOrder.ShowWorkingState
    }


    @SuppressLint("CheckResult")
    fun onLoginAction(username: String, password: String) {
        if(username.isBlank()) {
            toastData.asMut().value = "Username can't be left blank"
            return
        }

        if(password.isBlank()) {
            toastData.asMut().value = "Password can't be left blank"
            return
        }

        orderData.asMut().value = UiOrder.ShowLoadingState

        uRepo.loginOutstee(username, password)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    orderData.asMut().postValue(UiOrder.GoToChooseAvatar)
                },
                {
                    orderData.asMut().postValue(UiOrder.ShowWorkingState)
                    toastData.asMut().postValue(it.extractMessage())
                }
            )
    }
}