package com.anenigmatic.apogee19.screens.login.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.util.SingleLiveEvent
import com.anenigmatic.apogee19.screens.shared.util.asMut
import io.reactivex.schedulers.Schedulers

class ChooseLoginViewModel(private val uRepo: UserRepository) : ViewModel() {

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
    fun onLoginAction(idToken: String) {
        orderData.asMut().value = UiOrder.ShowLoadingState

        uRepo.loginBitsian(idToken)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    orderData.asMut().postValue(UiOrder.GoToChooseAvatar)
                },
                {
                    orderData.asMut().postValue(UiOrder.ShowWorkingState)
                    toastData.asMut().postValue("Something went wrong :/")
                }
            )
    }
}