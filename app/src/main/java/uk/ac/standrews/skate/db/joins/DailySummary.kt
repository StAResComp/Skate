package uk.ac.standrews.skate.db.joins

import java.text.DateFormat
import java.util.*

data class DailySummary(
    private val date: Date,
    private val summaryString: String
) {
    override fun toString(): String {
        val loc = Locale("en", "GB")
        val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc)
        return "${dateFormat.format(date)}: $summaryString"
    }
}