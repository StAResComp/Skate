package uk.ac.standrews.skate.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Species (
    @PrimaryKey val id: Int,
    val name: String
)