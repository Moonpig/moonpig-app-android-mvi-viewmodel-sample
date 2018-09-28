package com.moonpig.mvisample.domain.mvibase

interface BaseUseCase<A, R> {
    fun resultFrom(action: A): R
}