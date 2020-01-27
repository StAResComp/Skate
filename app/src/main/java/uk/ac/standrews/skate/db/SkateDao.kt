package uk.ac.standrews.skate.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uk.ac.standrews.skate.db.entities.Species

@Dao
interface SkateDao {

    @Insert
    fun insertSpecies(species: Array<Species>)

    @Query("SELECT * FROM species")
    fun getSpecies() : Array<Species>

}