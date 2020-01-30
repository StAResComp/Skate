package uk.ac.standrews.skate.db.joins

import uk.ac.standrews.skate.db.Exportable
import java.text.SimpleDateFormat
import java.util.*

data class SummaryForExport(
    private val date: Date,
    private val speciesName: String,
    private val otherName: String?,
    private val number: Int
) : Exportable {
    override fun toString(): String {
        val dateString = SimpleDateFormat("yyyy-MM-dd").format(date)
        var otherNameString = ""
        if (otherName != null) otherNameString = otherName
        return "$dateString, $speciesName, \"$otherNameString\", $number"
    }

    override fun toCsvString(): String {
        return toString()
    }

    override fun getCsvHeaderRow(): String {
        return "Date, Species, Other name, Number"
    }
}