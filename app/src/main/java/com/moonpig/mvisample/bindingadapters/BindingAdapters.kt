package com.moonpig.mvisample.bindingadapters

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.moonpig.mvisample.mvibase.Visibility
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url.isNullOrEmpty())
        return

    Picasso.get().load(url).into(view)
}

@BindingAdapter("mappedVisibility")
fun mapVisibility(view: View, visibility: Visibility?) {
    visibility?.toAndroidVisibility()?.let { view.visibility = it }
}

private fun Visibility.toAndroidVisibility() = when (this) {
    Visibility.GONE -> View.GONE
    Visibility.INVISIBLE -> View.INVISIBLE
    Visibility.VISIBLE -> View.VISIBLE
}