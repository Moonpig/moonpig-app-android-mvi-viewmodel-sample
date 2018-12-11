package com.moonpig.mvisample.productpicker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moonpig.mvisample.R
import com.moonpig.mvisample.productdetail.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_product_picker.success

class ProductPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_picker)

        success.setOnClickListener {
            ProductDetailActivity
                    .intentForProduct(this, "1")
                    .launch()
        }
    }

    private fun Intent.launch() = startActivity(this)
}
