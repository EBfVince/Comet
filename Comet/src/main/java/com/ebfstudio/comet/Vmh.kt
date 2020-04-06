package com.ebfstudio.comet

import androidx.lifecycle.*
import com.ebfstudio.comet.util.AppDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Deprecated(message = "Remplacé par SingleEventAction")
class Vmh<T>(
    private val viewModel: ViewModel,
    private val dispatchers: AppDispatchers,
    private val action: suspend () -> LiveData<T>
) {

    private var dataSource: LiveData<T> = MutableLiveData()
    private val _live = MediatorLiveData<Event<T>>()
    val live: LiveData<Event<T>> get() = _live

    fun process() = viewModel.viewModelScope.launch(dispatchers.main) {
        _live.removeSource(dataSource)
        dataSource = withContext(dispatchers.io) { action() }
        _live.addSource(dataSource) {
            _live.value = Event(it)
        }
    }

}