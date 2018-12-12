package com.moonpig.mvisample.bindingadapters

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.moonpig.mvisample.mvibase.Visibility
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url == null || url.isEmpty())
        return

    Picasso.get().load(url).into(view)
}

@BindingAdapter("mappedVisibility")
fun mapVisibility(view: View, visibility: Visibility?) {
    if (visibility == null || view.visibility == visibility.toAndroidVisibility())
        return

    view.visibility = visibility.toAndroidVisibility()
}

private fun Visibility.toAndroidVisibility() = when (this) {
    Visibility.GONE -> View.GONE
    Visibility.INVISIBLE -> View.INVISIBLE
    Visibility.VISIBLE -> View.VISIBLE
}