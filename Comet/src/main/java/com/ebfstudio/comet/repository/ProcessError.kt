package com.ebfstudio.comet.repository

/**
 * Created by Vince on 25/10/2019
 */
interface ProcessError<ErrorType> {
    fun find(e: Throwable): ErrorType
}