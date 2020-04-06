package com.ebfstudio.comet.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ebfstudio.comet.util.AbsentLiveData

inline fun <T, R> ViewModel.sbrrrl(
    liveData: LiveData<T>,
    crossinline action: (T) -> LiveData<R>
): LiveData<R> {
    return Transformations.switchMap(liveData) { data: T? ->
        if (data == null) {
            AbsentLiveData.create()
        } else {
            action(data)
        }
    }
}

fun <T : ViewModel, R> T.setIfNotSame(liveData: MutableLiveData<R>, data: R?): T {
    if (liveData.value != data) {
        liveData.value = data
    }
    return this
}