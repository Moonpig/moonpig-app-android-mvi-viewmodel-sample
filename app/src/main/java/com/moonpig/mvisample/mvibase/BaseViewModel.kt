package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BaseViewModel<I, VS>(private val baseIntentProcessor: BaseIntentProcessor<I, VS>) : ViewModel() {

    private val intentSubject = PublishSubject.create<I>()
    private val viewStateObservable = compose()

    fun processIntents(intents: Observable<I>) {
        intents.subscribe(intentSubject)
    }

    fun viewState(): Observable<VS> {
        return viewStateObservable
    }

    private fun compose() : Observable<VS> {
        return intentSubject.map { baseIntentProcessor.processIntent(it) }
    }

}

interface BaseIntentProcessor<I, VS> {
    fun processIntent(intent: I) : VS
}
