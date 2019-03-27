package com.anenigmatic.apogee19.screens.events.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.screens.events.data.EventRepository
import com.anenigmatic.apogee19.screens.shared.util.SingleLiveEvent
import com.anenigmatic.apogee19.screens.shared.util.asMut
import com.anenigmatic.apogee19.screens.shared.util.extractMessage
import com.anenigmatic.apogee19.screens.shared.util.set
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class EventListViewModel(private val eRepo: EventRepository) : ViewModel() {

    sealed class UiOrder {

        object ShowLoadingState : UiOrder()

        data class ShowWorkingState(val day0Events: List<Event>, val day1Events: List<Event>, val day2Events: List<Event>, val day3Events: List<Event>) : UiOrder()

        data class ShowFailureState(val message: String) : UiOrder()
    }


    val orderData: LiveData<UiOrder> = MutableLiveData()
    val toastData: LiveData<String?> = SingleLiveEvent()


    private val d1 = CompositeDisposable()


    init {
        // Initially the search text is assumed to be blank.
        loadEvents("")
    }


    fun onSearchTextChanged(searchText: String) {
        loadEvents(searchText)
    }

    @SuppressLint("CheckResult")
    fun starEvent(id: Long, value: Boolean) {
        eRepo.starEvent(id, value)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {

                },
                {
                    toastData.asMut().postValue(it.extractMessage())
                }
            )
    }


    private fun loadEvents(searchText: String) {
        d1.set(eRepo.getFilteredEvents()
            .map { events ->
                if(searchText.isNotBlank()) {
                    events.filter { it.name.toLowerCase().contains(searchText.toLowerCase()) }
                } else {
                    events
                }
            }
            .map<UiOrder> { events ->
                val day0Events = events.filter { it.date == LocalDate.of(2019, Month.MARCH, 28) }
                val day1Events = events.filter { it.date == LocalDate.of(2019, Month.MARCH, 29) }
                val day2Events = events.filter { it.date == LocalDate.of(2019, Month.MARCH, 30) }
                val day3Events = events.filter { it.date == LocalDate.of(2019, Month.MARCH, 31) }
                UiOrder.ShowWorkingState(day0Events, day1Events, day2Events, day3Events)
            }
            .startWith(UiOrder.ShowLoadingState)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { order ->
                    orderData.asMut().postValue(order)
                },
                { error ->
                    orderData.asMut().postValue(UiOrder.ShowFailureState(error.extractMessage()))
                }
            ))
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}