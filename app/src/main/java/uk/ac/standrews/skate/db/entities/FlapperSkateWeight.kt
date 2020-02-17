package uk.ac.standrews.skate.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "flapper_skate_weights")
data class FlapperSkateWeight(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sex: Char,
    val length: Int,
    val width: Int,
    val weight: Double
)