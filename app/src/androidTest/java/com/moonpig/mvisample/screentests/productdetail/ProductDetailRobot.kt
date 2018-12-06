package com.moonpig.mvisample.screentests.productdetail

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.moonpig.mvisample.R
import org.hamcrest.CoreMatchers.not

class ProductDetailRobot {
    fun isLoading() {
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }

    fun isNotLoading() {
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }
}
