package uk.ac.standrews.skate.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import uk.ac.standrews.skate.db.entities.Individual
import java.util.*

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = Individual::class,
            parentColumns = ["id"],
            childColumns = ["individualId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Photo (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "individual_id") val individualId: Int,
    val file: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") val modifiedAt: Date
)