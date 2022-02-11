package com.moonpig.mvisample.domain.mvibase

import io.reactivex.Observable

interface BaseUseCase<in Action : Any, out Result : Any> {
    fun resultFrom(action: Action): Observable<out Result>
}