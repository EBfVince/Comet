package com.ebfstudio.comet.action

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ebfstudio.comet.AbstractAction
import com.ebfstudio.comet.util.AppDispatchers
import com.ebfstudio.comet.SingleEvent
import com.ebfstudio.comet.SingleEventObserver
import com.ebfstudio.comet.repository.Resource

/**
 * Created by Vincent Guillebaud on 06/04/2020
 */
class SingleEventAction<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    AbstractAction<ResType, SingleEvent<ResType>>(d, vm, true) {

    override fun toOutType(value: ResType): SingleEvent<ResType> = SingleEvent(value)

    fun observeEvent(fragment: Fragment, onChange: (ResType) -> Unit) {
        observe(fragment, SingleEventObserver(onChange))
    }

}

data class SingleEvent<T>(
    val content: T
)

class SingleEventActionObserver<T>(private val onChange: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(t: SingleEvent<T>?) {
        t?.let { onChange(t.content) }
    }
}
