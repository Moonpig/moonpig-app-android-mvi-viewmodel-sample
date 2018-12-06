package com.moonpig.mvisample.screentests.productdetail

import android.support.test.espresso.intent.rule.IntentsTestRule
import com.moonpig.mvisample.productdetail.ProductDetailActivity

class ProductDetailTestRule :
        IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java, false, false)