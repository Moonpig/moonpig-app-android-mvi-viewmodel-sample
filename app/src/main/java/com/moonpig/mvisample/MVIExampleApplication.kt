package com.moonpig.mvisample

import android.app.Application
import com.moonpig.mvisample.di.ApplicationComponent
import com.moonpig.mvisample.di.DaggerApplicationComponent

open class MVIExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initialiseDagger()
    }

    private lateinit var _applicationComponent: ApplicationComponent
    open val applicationComponent
        get() = _applicationComponent

    private fun initialiseDagger() {
        _applicationComponent = DaggerApplicationComponent.create()
    }
}