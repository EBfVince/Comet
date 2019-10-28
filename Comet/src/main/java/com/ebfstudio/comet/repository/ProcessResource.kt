package com.ebfstudio.comet.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.SupervisorJob

abstract class ProcessResource<ResultType, RequestType, ErrorType, ResType : Resource<ResultType, ErrorType>> {

    protected val result = MutableLiveData<ResType>()
    protected val supervisorJob = SupervisorJob()

    fun asLiveData() = result as LiveData<ResType>

    @WorkerThread
    protected fun setValue(newValue: ResType) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    @WorkerThread
    protected abstract suspend fun createCallAsync(): RequestType

}