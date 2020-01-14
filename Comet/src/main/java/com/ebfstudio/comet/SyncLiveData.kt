package com.ebfstudio.comet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ebfstudio.comet.repository.Resource

/**
 * Created by Vincent Guillebaud on 21/11/2019
 */
class SyncLiveData {

    private val _mediator = MediatorLiveData<Boolean>().apply { value = false }
    val live: LiveData<Boolean> get() = _mediator

    private val list = arrayListOf<Plop>()

    fun add(liveData: LiveData<out Resource<*, *>>): SyncLiveData {

        val plop = Plop(liveData)
        list.add(plop)
        _mediator.addSource(plop.liveData) {
            it.onLoading {
                plop.ok = false
                process()
            }
            it.onFinish {
                plop.ok = true
                process()
            }
        }
        return this
    }

    fun reset() {
        for (p in list) {
            _mediator.removeSource(p.liveData)
        }
        list.clear()
        _mediator.value = false
    }

    private fun process() {

        var compteur = 0

        for (p in list) {
            if (p.ok) {
                compteur++
            }
        }

        _mediator.value = list.size == compteur

    }

    private class Plop(val liveData: LiveData<out Resource<*, *>>) {
        var ok = false
    }

}