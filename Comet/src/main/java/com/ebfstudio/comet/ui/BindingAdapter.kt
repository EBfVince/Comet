package com.ebfstudio.comet.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ebfstudio.comet.repository.Resource

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInvisible")
    fun showHide2(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {

        /* if (url.isNullOrBlank())
            return */

        Glide.with(imageView.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

    }

    @JvmStatic
    @BindingAdapter("items")
    fun <T, E> items(recyclerView: RecyclerView, resource: Resource<List<T>, E>?) {
        val adapter = recyclerView.adapter as? ListAdapter<T, *>
        adapter?.submitList(resource?.data)
    }

    @JvmStatic
    @BindingAdapter("items2")
    fun <T> items(recyclerView: RecyclerView, list: List<T>?) {
        val adapter = recyclerView.adapter as? ListAdapter<T, *>
        adapter?.submitList(list)
    }

}