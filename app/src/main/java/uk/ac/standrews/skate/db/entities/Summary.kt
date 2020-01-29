package uk.ac.standrews.skate.db.entities

import androidx.room.*
import uk.ac.standrews.skate.db.entities.Species
import java.util.*

@Entity(
    tableName = "summaries",
    foreignKeys = [
        ForeignKey(
            entity = Species::class,
            parentColumns = ["id"],
            childColumns = ["species_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("species_id")
    ]
)
data class Summary (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "species_id") var speciesId: Int?,
    @ColumnInfo(name = "other_name") var otherName: String?,
    var number: Int?,
    val date: Date,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") val modifiedAt: Date
)