package com.ebfstudio.comet.extension

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun DateTime.str(): String {
    val sdf = DateTimeFormat.forPattern("EEEE d MMMM")
    return toString(sdf).split(" ").joinToString(" ") { it.capitalize() }
}