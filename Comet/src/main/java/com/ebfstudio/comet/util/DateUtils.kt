package com.ebfstudio.comet.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object DateUtils {

    private const val FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss"

    /**
     * Formattage de la date suivant le pattern spécifié
     *
     * @param date La date à formater
     * @param pattern Le pattern de formatage de la date
     * @return La date au pattern choisi ou "" si date==null
     */
    private fun formatDate(date: DateTime?, pattern: String): String {
        val f = DateTimeFormat.forPattern(pattern)
        return if (date != null) f.print(date) else ""
    }

    /**
     * Formattage de la date suivant le pattern suivant : yyyy-MM-dd HH:mm:ss
     *
     * @param date La date à formater
     * @return La date au format yyyy-MM-dd ou "" si date==null
     */
    fun formatDateYMDHMS(date: DateTime): String =
        formatDate(
            date,
            FORMAT_YMDHMS
        )


}