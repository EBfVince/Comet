package com.ebfstudio.comet

import android.content.Context
import android.util.Log

/**
 * La classe resource permet de connaître le statut d'une donnée.
 * @param <T> Le type de l'objet dont on veut préciser le statut
</T> */
@Deprecated("remplacé")
data class Resource<out T> constructor(
    val status: Status,
    val data: T?,
    val errorReason: ErrorReason
) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                ErrorReason.UNKNOWN
            )
        }

        fun <T> error(error: ErrorReason, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                ErrorReason.UNKNOWN
            )
        }

    }

    fun getMessage(context: Context?): String {

        if (context == null) {
            Log.w("Resource", "getMessage(context) le context est null... relou --')")
            return ""
        }

        val ok: Int = 0/* when (errorReason) {
            ErrorReason.UNKNOWN -> R.string.erreur_inconnue
            ErrorReason.NO_INTERNET -> R.string.erreur_connexion_internet
            ErrorReason.API_RESPONSE_ERROR -> R.string.erreur_api
            ErrorReason.NO_RESPONSE -> R.string.erreur_serveur_inaccessible
            ErrorReason.NO_PREVIOUS_CACHED_USER -> R.string.erreur_inconnue
        }*/

        return context.getString(ok)

    }

    fun onSuccess(f: (T) -> Unit): Resource<T> {
        if (status == Status.SUCCESS && data != null) {
            f(data)
        }
        return this
    }

    fun onError(f: (ErrorReason) -> Unit): Resource<T> {
        if (status == Status.ERROR) {
            f(errorReason)
        }
        return this
    }

}