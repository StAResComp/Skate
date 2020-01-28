package uk.ac.standrews.skate.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.standrews.skate.db.entities.*

@Dao
interface SkateDao {

    @Insert
    fun insertSpecies(species: Array<Species>)

    @Insert
    fun insertEffort(effort: Effort)

    @Update
    fun updateEffort(effort: Effort)

    @Query("SELECT * FROM species")
    fun getSpecies() : Array<Species>

    @Query("SELECT * FROM effort WHERE finished_at IS NOT NULL ORDER BY finished_at DESC")
    fun getPreviousEffort() : LiveData<Array<Effort>>

    @Query("SELECT * FROM effort ORDER BY started_at DESC LIMIT 1")
    fun getLastEffort() : LiveData<Effort>

    @Query("SELECT * FROM effort WHERE id = :id")
    fun getEffortById(id: Int) : Effort?

}