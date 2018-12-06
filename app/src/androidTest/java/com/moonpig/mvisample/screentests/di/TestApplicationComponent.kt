package com.moonpig.mvisample.screentests.di

import com.moonpig.mvisample.di.ApplicationComponent
import dagger.Component

@Component(modules = [
    MockProductDetailDataModule::class
])
interface TestApplicationComponent : ApplicationComponent