package com.ebfstudio.comet.action

import androidx.lifecycle.ViewModel
import com.ebfstudio.comet.util.AppDispatchers
import com.ebfstudio.comet.repository.Resource

/**
 * Created by Vincent Guillebaud on 06/04/2020
 */
class Action<T, E, ResType : Resource<T, E>>(d: AppDispatchers, vm: ViewModel) :
    AbstractAction<ResType, ResType>(d, vm) {
    override fun toOutType(value: ResType): ResType = value
}