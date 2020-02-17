package uk.ac.standrews.skate.db.joins

import uk.ac.standrews.skate.db.Exportable
import java.text.SimpleDateFormat
import java.util.*

data class IndividualForExport(
    private val date: Date,
    private val speciesName: String,
    private val length: Double,
    private val width: Double,
    private val sex: Char,
    private val weight: Double,
    private val pitTagNumber: String,
    private val photos: String?
) : Exportable {
    override fun toString(): String {
        val dateString = SimpleDateFormat("yyyy-MM-dd").format(date)
        return "$dateString, $speciesName, $length, $width, $sex, $weight, \"$pitTagNumber\", \"$photos\""
    }

    override fun toCsvString(): String {
        return toString()
    }

    override fun getCsvHeaderRow(): String {
        return "Date, Species, Length, Width, Sex, Weight, PIT tag number, Photos"
    }
}