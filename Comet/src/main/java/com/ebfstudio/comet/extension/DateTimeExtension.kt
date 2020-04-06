package com.ebfstudio.comet.extension

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

@ExperimentalStdlibApi
fun DateTime.str(): String {
    val sdf = DateTimeFormat.forPattern("EEEE d MMMM")
    return toString(sdf).split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }
}
