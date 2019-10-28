package com.ebfstudio.comet.repository

import android.util.Log


/**
 * La classe resource permet de connaître le statut d'une donnée.
 * @param <T> Le type de l'objet dont on veut préciser le statut
</T> */
abstract class Resource<DataType, ErrorType>(
    val status: Status,
    val data: DataType?,
    val error: ErrorType
) {

    fun onSuccess(f: (DataType) -> Unit): Resource<DataType, ErrorType> {
        if (status == Status.SUCCESS && data != null) {
            f(data)
        } else if (status == Status.SUCCESS && data == null) {
            Log.w(
                "Resource",
                "Attention, la ressource est en SUCCESS mais la data est NULL"
            )
        }
        return this
    }

    fun onLoading(f: () -> Unit): Resource<DataType, ErrorType> {
        if (status == Status.LOADING) {
            f()
        }
        return this
    }

    fun onError(f: (ErrorType) -> Unit): Resource<DataType, ErrorType> {
        if (status == Status.ERROR) {
            f(error)
        }
        return this
    }

    fun isValueNull() = data == null

}