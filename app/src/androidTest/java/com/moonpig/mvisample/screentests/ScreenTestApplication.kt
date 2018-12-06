package com.moonpig.mvisample.screentests

import android.content.Context
import com.moonpig.mvisample.MVIExampleApplication
import com.moonpig.mvisample.di.ApplicationComponent

class ScreenTestApplication : MVIExampleApplication() {

    init {
        _instance = this
    }

    override val applicationComponent: ApplicationComponent
        get() = overriddenApplicationComponent
                ?: super.applicationComponent

    companion object {
        var overriddenApplicationComponent: ApplicationComponent? = null
        private lateinit var _instance: Context
        val instance: Context
            get() = _instance
    }
}