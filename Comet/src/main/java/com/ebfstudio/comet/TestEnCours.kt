package com.ebfstudio.comet

import androidx.fragment.app.Fragment
import com.ebfstudio.comet.repository.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

/**
 * Created by Vince on 28/08/2019.
 * TODO trouver un nom jpep
 */

abstract class EpicBro<InType, OutType>(
    private val dispatchers: AppDispatchers,
    private val viewModel: ViewModel,
    isEvent: Boolean = false
) {

    private val _data = MediatorLiveData<OutType>()
    val data: LiveData<OutType> = if (isEvent) {
        _data.toSingleEvent()
    } else {
        _data
    }

    private var dataSource: LiveData<InType> = MutableLiveData()
    private var cacheF: suspend () -> LiveData<InType> = { MutableLiveData<InType>() }
    private var job: Job? = null

    abstract fun toOutType(value: InType): OutType

    fun trigger(f: suspend () -> LiveData<InType>) {

        // pour éviter les problèmes, si une tâche est déjà encours, on la cancel.
        // lors d'un double appel à trigger par erreur, on ne fera qu'une seule fois l'action 👌
        if (job?.isActive == true)
            job?.cancel()

        job = viewModel.viewModelScope.launch(dispatchers.main) {

            // On supprime toutes les sources
            _data.removeSource(dataSource)

            // On execute l'action
            dataSource = withContext(dispatchers.io) { f() }

            // On observe chaque changement qu'on transmet à data
            _data.addSource(dataSource) { value ->
                _data.value = toOutType(value)
            }

            // On garde en cache l'action
            cacheF = f

        }

    }


    fun retry() = trigger(cacheF)

    open fun observe(fragment: Fragment, onChanged: (OutType) -> Unit) {
        data.observe(fragment.viewLifecycleOwner, onChanged)
    }

    open fun observe(fragment: Fragment, observer: Observer<OutType>) {
        data.observe(fragment.viewLifecycleOwner, observer)
    }

}

@Deprecated("Maybe try to use \"SingleEventBro\"")
class EventBro<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    EpicBro<ResType, Event<ResType>>(d, vm) {
    override fun toOutType(value: ResType): Event<ResType> = Event(value)

    fun observeEvent(fragment: Fragment, onChanged: (ResType) -> Unit) {
        observe(fragment, EventObserver { onChanged(it) })
    }

    fun observeTest(fragment: Fragment, onChanged: (ResType) -> Unit) {
        val live = Transformations.map(data) { it.peekContent() }
        live.observe(fragment.viewLifecycleOwner, onChanged)
    }

}

class SameBro<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    EpicBro<ResType, ResType>(d, vm) {
    override fun toOutType(value: ResType): ResType = value
}

class SingleEventBro<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    EpicBro<ResType, SingleEvent<ResType>>(d, vm, true) {

    override fun toOutType(value: ResType): SingleEvent<ResType> = SingleEvent(value)

    fun observeEvent(fragment: Fragment, onChange: (ResType) -> Unit) {
        observe(fragment, SingleEventObserver(onChange))
    }

}

data class SingleEvent<T>(
    val content: T
)

class SingleEventObserver<T>(private val onChange: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(t: SingleEvent<T>?) {
        t?.let { onChange(t.content) }
    }
}