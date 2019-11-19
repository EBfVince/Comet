package com.ebfstudio.comet.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.ebfstudio.comet.*
import com.ebfstudio.comet.navigation.NavigationCommand
import com.ebfstudio.comet.repository.Resource

abstract class BaseViewModel(private val dispatchers: AppDispatchers) : ViewModel() {

    // FOR NAVIGATION
    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        _navigation.postValue(Event(NavigationCommand.To(directions)))
    }

    fun navigateBack() {
        _navigation.postValue(Event(NavigationCommand.Back))
    }


    protected fun <T, E, ResType : Resource<T, E>> broSo() =
        EventBro<T, E, ResType>(dispatchers, this)

    protected fun <T, E, ResType : Resource<T, E>> broSo2() =
        SameBro<T, E, ResType>(dispatchers, this)

    protected fun <T, E, ResType : Resource<T, E>> broSo3() =
        SingleEventBro<T, E, ResType>(dispatchers, this)

}