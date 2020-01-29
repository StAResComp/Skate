package uk.ac.standrews.skate.db.entities

import androidx.room.*
import java.text.DateFormat
import java.util.*

@Entity(
    tableName = "individuals",
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
class Individual (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "species_id") val speciesId: Int,
    val date: Date,
    val length: Double,
    val width: Double,
    val sex: Char,
    @ColumnInfo(name = "pit_tag_number") val pitTagNumber: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "modified_at") val modifiedAt: Date
) {
    override fun toString(): String {
        val loc = Locale("en", "GB")
        val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc)
        return "${dateFormat.format(date)}: ${length}×${width}, $sex, $pitTagNumber"
    }
}