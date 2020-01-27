package uk.ac.standrews.skate.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Effort (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "started_at") val startedAt: Date,
    @ColumnInfo(name = "starting_latitude") val startingLatitude: Double,
    @ColumnInfo(name = "starting_longitude") val startingLongitude: Double,
    @ColumnInfo(name = "finished_at") val finishedAt: Date?,
    @ColumnInfo(name = "finishing_latitude") val finishingLatitude: Double?,
    @ColumnInfo(name = "finishing_longitude") val finishingLongitude: Double?,
    @ColumnInfo(name = "num_rods") val numRods: Int,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") val modifiedAt: Date
)