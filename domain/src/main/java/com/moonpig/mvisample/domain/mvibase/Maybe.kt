package com.moonpig.mvisample.domain.mvibase

sealed class Maybe<out T: Any> {
    object Empty : Maybe<Nothing>()

    data class Just<T: Any>(val value: T) : Maybe<T>()
}