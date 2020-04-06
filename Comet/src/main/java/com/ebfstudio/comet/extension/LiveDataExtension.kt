package com.ebfstudio.comet.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.hadilq.liveevent.LiveEvent

fun <T> MutableLiveData<T>.reload() {
    this.value?.let {
        this.value = value
    }
}

inline fun <T> LiveData<T>.observe(fragment: Fragment, crossinline onChanged: (T) -> Unit) {
    this.observe(fragment.viewLifecycleOwner, Observer {
        it?.let(onChanged)
    })
}

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}
