package com.ebfstudio.comet

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.ebfstudio.comet.repository.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Vince on 28/08/2019.
 * TODO trouver un nom jpep
 */

abstract class EpicBro<InType, OutType>(
    private val dispatchers: AppDispatchers,
    private val viewModel: ViewModel
) {

    private val _data = MediatorLiveData<OutType>()
    val data: LiveData<OutType> get() = _data

    private var dataSource: LiveData<InType> = MutableLiveData()
    private var cacheF: suspend () -> LiveData<InType> = { MutableLiveData<InType>() }

    abstract fun toOutType(value: InType): OutType

    fun trigger(f: suspend () -> LiveData<InType>) =
        viewModel.viewModelScope.launch(dispatchers.main) {

            // On supprime toutes les sources
            _data.removeSource(dataSource)

            dataSource = withContext(dispatchers.io) { f() }

            _data.addSource(dataSource) { value ->
                _data.value = toOutType(value)
            }

        }.also { cacheF = f }

    fun retry() = trigger(cacheF)

    fun observe(fragment: Fragment, onChanged: (OutType) -> Unit) {
        data.observe(fragment.viewLifecycleOwner, onChanged)
    }

    fun observe(fragment: Fragment, observer: Observer<OutType>) {
        data.observe(fragment.viewLifecycleOwner, observer)
    }

}

class EventBro<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    EpicBro<ResType, Event<ResType>>(d, vm) {
    override fun toOutType(value: ResType): Event<ResType> = Event(value)

    fun observeEvent(fragment: Fragment, onChanged: (ResType) -> Unit) {
        observe(fragment, EventObserver { onChanged(it) })
    }
}

class SameBro<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    EpicBro<ResType, ResType>(d, vm) {
    override fun toOutType(value: ResType): ResType = value
}

