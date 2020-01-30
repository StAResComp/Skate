package uk.ac.standrews.skate.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.standrews.skate.db.Exportable
import java.text.DateFormat
import java.util.*

@Entity
data class Effort (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "started_at") val startedAt: Date,
    @ColumnInfo(name = "starting_latitude") val startingLatitude: Double,
    @ColumnInfo(name = "starting_longitude") val startingLongitude: Double,
    @ColumnInfo(name = "finished_at") var finishedAt: Date?,
    @ColumnInfo(name = "finishing_latitude") var finishingLatitude: Double?,
    @ColumnInfo(name = "finishing_longitude") var finishingLongitude: Double?,
    @ColumnInfo(name = "num_rods") val numRods: Int,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") var modifiedAt: Date
) : Exportable {
    override fun toString(): String {
        val loc = Locale("en", "GB")
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, loc)
        val startTime = dateFormat.format(startedAt)
        val startLat = "%.2f".format(startingLatitude)
        val startLon = "%.2f".format(startingLongitude)
        if (finishedAt != null) {
            val finishTime = dateFormat.format(finishedAt)
            val finishLat = "%.2f".format(finishingLatitude)
            val finishLon = "%.2f".format(finishingLongitude)
            return "$startTime/$startLat,$startLon - $finishTime/$finishLat,$finishLon ($numRods rods)"
        }
        else {
            return "Started at $startTime/$startLat,$startLon ($numRods rods)"
        }
    }

    override fun getCsvHeaderRow(): String {
        return "Start time, Start lat, Start lon, Finish time, Finish lat, Finish lon, Num rods"
    }

    override fun toCsvString(): String {
        return "${startedAt.toString()},$startingLatitude,$startingLongitude,${finishedAt.toString()},$finishingLatitude,$finishingLongitude,$numRods"
    }
}