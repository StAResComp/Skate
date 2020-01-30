package uk.ac.standrews.skate.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.standrews.skate.db.entities.*
import uk.ac.standrews.skate.db.joins.DailySummary

@Dao
interface SkateDao {

    @Insert
    fun insertSpecies(species: Array<Species>)

    @Insert
    fun insertEffort(effort: Effort)

    @Insert
    fun insertSummaries(summaries: List<Summary>)

    @Insert
    fun insertIndividual(individual: Individual)

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

    @Query("SELECT * FROM summaries ORDER BY date DESC, species_id ASC")
    fun getSummaries(): LiveData<Array<Summary>>

    @Query("SELECT id FROM species WHERE name LIKE 'Flapper skate'")
    fun getFlapperSkateId(): Int

    @Query("SELECT * FROM individuals ORDER BY date DESC")
    fun getIndividuals(): LiveData<Array<Individual>>

    @Query("SELECT summaries.date AS date, GROUP_CONCAT(summaries.number || ' ' || species.name || COALESCE(' (' || summaries.other_name || ')', ''), ', ') AS summaryString FROM summaries JOIN species ON summaries.species_id = species.id GROUP BY summaries.date ORDER BY summaries.date DESC")
    fun getDailySummaries(): LiveData<Array<DailySummary>>
}