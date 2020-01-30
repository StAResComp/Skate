package uk.ac.standrews.skate.db.joins

import java.text.SimpleDateFormat
import java.util.*

data class IndividualForExport(
    private val date: Date,
    private val speciesName: String,
    private val length: Double,
    private val width: Double,
    private val sex: Double,
    private val pitTagNumber: String
) {
    override fun toString(): String {
        val dateString = SimpleDateFormat("yyyy-MM-dd").format(date)
        return "$dateString, $speciesName, $length, $width, $sex \"$pitTagNumber\""
    }

    fun getCsvHeaderRow(): String {
        return "Date, Species, Length, Width, Sex, PIT tag number"
    }
}