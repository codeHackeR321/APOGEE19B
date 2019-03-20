package com.anenigmatic.apogee19.screens.login.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.util.SingleLiveEvent
import com.anenigmatic.apogee19.screens.shared.util.asMut
import io.reactivex.schedulers.Schedulers

class ChooseAvatarViewModel(private val uRepo: UserRepository) : ViewModel() {

    sealed class UiOrder {

        object ShowLoadingState : UiOrder()

        data class ShowWorkingState(val avatars: List<Avatar>, val chosenAvatarId: Long) : UiOrder()

        object GoToDesiredScreen : UiOrder()
    }


    val orderData: LiveData<UiOrder> = MutableLiveData()
    val toastData: LiveData<String?> = SingleLiveEvent()


    init {
        orderData.asMut().value = UiOrder.ShowLoadingState

        uRepo.getAllAvatars()
            .flatMapSingle { avatars ->
                uRepo.getUser()
                    .toSingle()
                    .map { user -> user.avatarId }
                    .map { chosenAvatarId -> UiOrder.ShowWorkingState(avatars, chosenAvatarId) }
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { order ->
                    orderData.asMut().postValue(order)
                },
                {
                    toastData.asMut().postValue("Something went wrong. Please restart the app")
                }
            )
    }


    fun onChooseAvatarAction(avatarId: Long) {
        (orderData.value as? UiOrder.ShowWorkingState)?.avatars?.let { avatars ->
            orderData.asMut().value = UiOrder.ShowWorkingState(avatars, avatarId)
        }
    }

    fun onProceedAction() {
        (orderData.value as? UiOrder.ShowWorkingState)?.chosenAvatarId?.let { chosenAvatarId ->
            val backupOrder = orderData.value

            orderData.asMut().value = UiOrder.ShowLoadingState

            uRepo.chooseAvatar(chosenAvatarId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        orderData.asMut().postValue(UiOrder.GoToDesiredScreen)
                    },
                    {
                        orderData.asMut().postValue(backupOrder)
                        toastData.asMut().postValue("Something went wrong :/")
                    }
                )
        }
    }
}