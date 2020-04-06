package com.ebfstudio.comet.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.ebfstudio.comet.action.Action
import com.ebfstudio.comet.action.ActionSingleEvent
import com.ebfstudio.comet.navigation.NavigationCommand
import com.ebfstudio.comet.repository.Resource
import com.ebfstudio.comet.util.AppDispatchers
import com.hadilq.liveevent.LiveEvent

abstract class BaseViewModel(private val dispatchers: AppDispatchers) : ViewModel() {

    // FOR NAVIGATION
    private val _navigation = LiveEvent<NavigationCommand>()
    val navigation: LiveData<NavigationCommand> = _navigation

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        _navigation.postValue(NavigationCommand.To(directions))
    }

    fun navigateBack() {
        _navigation.postValue(NavigationCommand.Back)
    }


    protected fun <T, E, ResType : Resource<T, E>> createAction() =
        Action<T, E, ResType>(dispatchers, this)

    protected fun <T, E, ResType : Resource<T, E>> createSingleEventAction() =
        ActionSingleEvent<T, E, ResType>(dispatchers, this)

}