package com.ebfstudio.comet.action

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.ebfstudio.comet.util.AppDispatchers
import com.ebfstudio.comet.extension.toSingleEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Vincent Guillebaud on 06/04/2020
 */
abstract class AbstractAction<InType, OutType>(
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
    private var cacheF: suspend () -> LiveData<InType> = { MutableLiveData() }
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
