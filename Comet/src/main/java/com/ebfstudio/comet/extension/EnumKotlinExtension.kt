package com.ebfstudio.comet.extension

inline fun <reified T : Enum<T>> safeEnumValueOf(type: String?, defaultEnum: T): T {
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        defaultEnum
    }
}
