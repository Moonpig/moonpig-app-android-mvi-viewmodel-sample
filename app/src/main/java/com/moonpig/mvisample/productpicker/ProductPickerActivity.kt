package com.moonpig.mvisample.productpicker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moonpig.mvisample.R
import com.moonpig.mvisample.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_product_picker.error
import kotlinx.android.synthetic.main.activity_product_picker.success

/**
 * This class does not follow the MVI pattern.
 * This was a deliberate decision as it should only help with putting the {ProductDetailActivity}
 * into different states by passing different productIds which are interpreted by the fake
 * data source.
 */
class ProductPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_picker)

        success.setOnClickListener {
            ProductDetailActivity
                    .intentForProduct(this, catArmourProductId)
                    .launch()
        }

        error.setOnClickListener {
            ProductDetailActivity
                    .intentForProduct(this, notExistingProductId)
                    .launch()
        }
    }

    private fun Intent.launch() = startActivity(this)

    companion object {
        private const val catArmourProductId = "1"
        private const val notExistingProductId = "!@£$%"
    }
}
