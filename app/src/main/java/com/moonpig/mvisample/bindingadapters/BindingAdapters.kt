package com.moonpig.mvisample.bindingadapters

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url == null || url.isEmpty())
        return

    Picasso.get().load(url).into(view)
}