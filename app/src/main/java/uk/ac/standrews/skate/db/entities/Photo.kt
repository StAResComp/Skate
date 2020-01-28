package uk.ac.standrews.skate.db.entities

import androidx.room.*
import androidx.room.util.TableInfo
import uk.ac.standrews.skate.db.entities.Individual
import java.util.*

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = Individual::class,
            parentColumns = ["id"],
            childColumns = ["individual_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("individual_id")
    ]
)
data class Photo (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "individual_id") val individualId: Int,
    val file: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") val modifiedAt: Date
)