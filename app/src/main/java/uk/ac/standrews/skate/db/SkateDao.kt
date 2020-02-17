package uk.ac.standrews.skate.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.standrews.skate.db.entities.*
import uk.ac.standrews.skate.db.joins.DailySummary
import uk.ac.standrews.skate.db.joins.IndividualForExport
import uk.ac.standrews.skate.db.joins.SummaryForExport

@Dao
interface SkateDao {

    @Insert
    fun insertSpecies(species: Array<Species>)

    @Insert
    fun insertWeights(weights: Array<FlapperSkateWeight>)

    @Insert
    fun insertEffort(effort: Effort)

    @Insert
    fun insertSummaries(summaries: List<Summary>)

    @Insert
    fun insertIndividual(individual: Individual): Long

    @Insert
    fun insertPhotos(photos: List<Photo>)

    @Update
    fun updateEffort(effort: Effort)

    @Query("SELECT * FROM species")
    fun getSpecies() : Array<Species>

    @Query("SELECT * FROM effort WHERE finished_at IS NOT NULL ORDER BY finished_at DESC")
    fun getPreviousEffort() : LiveData<Array<Effort>>

    @Query("SELECT * FROM effort ORDER BY started_at DESC LIMIT 1")
    fun getLastEffort() : LiveData<Effort>

    @Query("SELECT * FROM effort ORDER BY finished_at ASC")
    fun getEffort(): List<Effort>

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

    @Query("SELECT summaries.date AS date, species.name AS speciesName, summaries.other_name AS otherName, summaries.number AS number FROM summaries JOIN species ON summaries.species_id = species.id ORDER BY summaries.date ASC")
    fun getSummariesForExport(): List<SummaryForExport>

    @Query("SELECT individuals.date AS date, species.name AS speciesName, individuals.length AS length, individuals.width AS width, individuals.sex AS sex, individuals.pit_tag_number AS pitTagNumber, GROUP_CONCAT(photos.file, '; ') AS photos FROM individuals JOIN species ON individuals.species_id = species.id LEFT OUTER JOIN photos ON individuals.id = photos.individual_id GROUP BY date, speciesName, length, width, sex, pit_tag_number ORDER BY individuals.date ASC")
    fun getIndividualsForExport(): List<IndividualForExport>

    @Query("SELECT COUNT(*) FROM flapper_skate_weights")
    fun getNumWeights(): Int

    @Query("SELECT weight FROM flapper_skate_weights WHERE sex = :sex AND length = :length AND width = :width")
    fun getWeight(sex: Char, length: Int, width: Int): Double?
}