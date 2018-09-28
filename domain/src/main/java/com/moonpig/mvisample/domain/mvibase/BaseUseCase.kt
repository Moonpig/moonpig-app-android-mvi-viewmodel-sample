package com.moonpig.mvisample.domain.mvibase

import io.reactivex.Observable

interface BaseUseCase<A, R> {
    fun resultFrom(action: A): Observable<R>
}