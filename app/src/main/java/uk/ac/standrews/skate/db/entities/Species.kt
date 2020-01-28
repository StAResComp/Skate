package uk.ac.standrews.skate.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Species (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)