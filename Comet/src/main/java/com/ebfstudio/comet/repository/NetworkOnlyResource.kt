package com.ebfstudio.comet.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

abstract class NetworkOnlyResource<ResultType, RequestType, ErrorType, ResType : Resource<ResultType, ErrorType>>
    : ProcessResource<ResultType, RequestType, ErrorType, ResType>() {

    private suspend fun build(): NetworkOnlyResource<ResultType, RequestType, ErrorType, ResType> {

        withContext(Dispatchers.Main) {
            result.value = resFactory.loading(null)
        }

        CoroutineScope(coroutineContext).launch(supervisorJob) {

            try {

                // Execute l'appel API
                val rep = createCallAsync() ?: throw Exception("A traiter")

                Log.d("NetworkOnlyResource", "Res = $rep")

                if (isSuccessful(rep)) {
                    setValue(resFactory.success(onSuccess(rep)))
                } else {
                    setValue(resFactory.error(onError(rep)))
                }

            } catch (e: Exception) {

                // Log.e("NetworkOnlyResource", "${e.message}")

                val error = onError(e)
                setValue(resFactory.error(error))

            }

        }

        return this

    }

    suspend fun create(): LiveData<ResType> = build().asLiveData()


    protected abstract val resFactory: ResourceFactory<ResultType, ErrorType, ResType>

    protected abstract fun isSuccessful(data: RequestType): Boolean
    protected abstract fun onSuccess(data: RequestType): ResultType

    protected abstract fun onError(data: RequestType): ErrorType
    protected abstract fun onError(e: Throwable): ErrorType

}