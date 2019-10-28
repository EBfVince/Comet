package com.ebfstudio.comet.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType, ErrorType, ResType : Resource<ResultType, ErrorType>>(
    private val resFactory: ResourceFactory<ResultType, ErrorType, ResType>,
    private val detectError: ProcessError<ErrorType>
) : ProcessResource<ResultType, RequestType, ErrorType, ResType>() {

    suspend fun build(): NetworkBoundResource<ResultType, RequestType, ErrorType, ResType> {

        // Loading data
        withContext(Dispatchers.Main) {
            result.value = resFactory.loading(null)
        }

        // En tâche de fond
        CoroutineScope(coroutineContext).launch(supervisorJob) {

            // On récupère le cache du tel
            val dbResult = loadFromDb()

            // Actualiser les données depuis internet ?
            if (shouldFetch(dbResult)) {

                // Dispatch latest value quickly (UX purpose)
                setValue(resFactory.loading(dbResult))
                fetchFromNetwork()

            } else {
                setValue(resFactory.success(dbResult))
            }
        }

        return this

    }


    // ---

    private suspend fun fetchFromNetwork() {

        try {

            // Execute l'appel API
            val rep = createCallAsync() ?: throw Exception("A traiter")

            // Log.d("NetworkBoundResource", "Res = $rep")

            // Enregistre en cache les résultats
            saveCallResults(processResponse(rep))

            // Récupère depuis le cache les derniers résultats
            setValue(resFactory.success(loadFromDb()))

        } catch (e: Exception) {

            // Log.e("NetworkBoundResource", "${e.message}")

            // Erreur, on affiche le cache
            val error = detectError.find(e)
            setValue(resFactory.error(error, loadFromDb()))

        }

    }


    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract suspend fun loadFromDb(): ResultType?

}