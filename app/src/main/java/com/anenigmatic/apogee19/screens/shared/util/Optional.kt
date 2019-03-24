package com.anenigmatic.apogee19.screens.shared.util

sealed class Optional<out T: Any> {

    data class Some<out T: Any>(val value: T) : Optional<T>()

    object None : Optional<Nothing>()
}