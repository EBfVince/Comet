package com.ebfstudio.comet.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

abstract class NetworkOnlyResource<ResultType, RequestType, ErrorType, ResType : Resource<ResultType, ErrorType>>(
    private val resFactory: ResourceFactory<ResultType, ErrorType, ResType>,
    private val detectError: ProcessError<ErrorType>
) : ProcessResource<ResultType, RequestType, ErrorType, ResType>() {

    suspend fun build(): NetworkOnlyResource<ResultType, RequestType, ErrorType, ResType> {

        withContext(Dispatchers.Main) {
            result.value = resFactory.loading(null)
        }

        CoroutineScope(coroutineContext).launch(supervisorJob) {

            try {

                // Execute l'appel API
                val rep = createCallAsync() ?: throw Exception("A traiter")

                Log.d("NetworkOnlyResource", "Res = $rep")

                // Transforme le résultat pour avoir l'objet voulu
                val data = transform(rep)

                // Envoie la réponse
                setValue(resFactory.success(data))

            } catch (e: Exception) {

                //Log.e("NetworkOnlyResource", "${e.message}")
                e.printStackTrace()

                val error = detectError.find(e)
                setValue(resFactory.error(error, null))

            }

        }

        return this

    }

    protected abstract suspend fun transform(data: RequestType): ResultType

}