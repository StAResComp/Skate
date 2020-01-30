package uk.ac.standrews.skate.db

interface Exportable {

    fun getCsvHeaderRow(): String

    fun toCsvString(): String
}