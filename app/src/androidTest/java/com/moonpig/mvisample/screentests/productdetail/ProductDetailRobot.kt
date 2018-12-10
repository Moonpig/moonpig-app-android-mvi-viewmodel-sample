package com.moonpig.mvisample.screentests.productdetail

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.moonpig.mvisample.R

class ProductDetailRobot {
    fun isLoading() = apply {
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(VISIBLE)))
    }

    fun isNotLoading() = apply {
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(GONE)))
    }

    fun nameDisplayed(name: String) = apply {
        onView(withId(R.id.name)).check(matches(withText(name)))
    }

    fun descriptionDisplayed(description: String) = apply {
        onView(withId(R.id.description)).check(matches(withText(description)))
    }

    fun priceDisplayed(price: String) = apply {
        onView(withId(R.id.price)).check(matches(withText(price)))
    }
}
