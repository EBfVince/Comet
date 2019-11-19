package com.ebfstudio.comet.repository

/**
 * Created by Vince on 25/10/2019
 */
interface ResourceFactory<DataType, ErrorType, ResType : Resource<DataType, ErrorType>> {
    fun success(data: DataType?): ResType
    fun loading(data: DataType?): ResType
    fun error(error: ErrorType, data: DataType? = null): ResType
}
