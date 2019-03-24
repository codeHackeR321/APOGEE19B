package com.anenigmatic.apogee19.screens.profile.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(private val uRepo: UserRepository) : ViewModel() {

    sealed class UiOrder {

        object ShowLoadingState : UiOrder()

        data class ShowWorkingState(val user: User) : UiOrder()

        object GoToLoginScreen : UiOrder()
    }


    val orderData: LiveData<UiOrder> = MutableLiveData()
    val toastData: LiveData<String?> = SingleLiveEvent()


    private val d1 = CompositeDisposable()


    init {
        orderData.asMut().value = UiOrder.ShowLoadingState

        d1.set(uRepo.getUser()
            .map { user ->
                when(user) {
                    is Optional.Some -> UiOrder.ShowWorkingState(user.value)
                    is Optional.None -> UiOrder.GoToLoginScreen
                }
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { order ->
                    orderData.asMut().postValue(order)
                },
                {
                    toastData.asMut().postValue("Something went wrong :/")
                }
            ))
    }


    fun onQrCodeRefreshAction() {
        val backupOrder = orderData.value

        orderData.asMut().value = UiOrder.ShowLoadingState
    }

    @SuppressLint("CheckResult")
    fun onAddMoneyAction(amount: Int) {
        if(amount <= 0) {
            toastData.asMut().postValue("You're only allowed to enter a positive amount of money")
            return
        }

        val backupOrder = orderData.value

        orderData.asMut().value = UiOrder.ShowLoadingState
        uRepo.addMoney(amount)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    // Since we're observing uRepo.getUser in init, change in money will be automatically be reflected.
                    toastData.asMut().postValue("Successfully added money")
                },
                {
                    orderData.asMut().postValue(backupOrder)
                    toastData.asMut().postValue("Something went wrong :/")
                }
            )
    }

    @SuppressLint("CheckResult")
    fun onTransferMoneyAction(amount: Int, receivingQrCode: String) {
        if(amount <= 0) {
            toastData.asMut().postValue("You're only allowed to enter a positive amount of money")
            return
        }

        val backupOrder = orderData.value

        orderData.asMut().value = UiOrder.ShowLoadingState
        uRepo.transferMoney(amount, receivingQrCode)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    // Since we're observing uRepo.getUser in init, change in money will be automatically be reflected.
                    toastData.asMut().postValue("Successfully transferred money")
                },
                {
                    orderData.asMut().postValue(backupOrder)
                    toastData.asMut().postValue("Something went wrong :/")
                }
            )
    }

    @SuppressLint("CheckResult")
    fun onLogoutAction() {
        val backupOrder = orderData.value

        orderData.asMut().value = UiOrder.ShowLoadingState
        uRepo.logout()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    toastData.asMut().postValue("You are now logged-out.")
                },
                {
                    orderData.asMut().postValue(backupOrder)
                    toastData.asMut().postValue("Something went wrong :/")
                }
            )
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}