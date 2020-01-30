package uk.ac.standrews.skate.ui.summary

import android.app.Application
import android.util.ArrayMap
import android.util.Log
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import uk.ac.standrews.skate.db.SkateDatabase
import uk.ac.standrews.skate.db.entities.Species
import uk.ac.standrews.skate.db.entities.Summary
import uk.ac.standrews.skate.db.joins.DailySummary
import java.text.DateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class SummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()
    private lateinit var species: Array<Species>

    fun getSpecies(): Array<Species> {
        if (!::species.isInitialized) {
            val c = Callable {
                dao.getSpecies()
            }
            species = Executors.newSingleThreadExecutor().submit(c).get()
        }
        return species
    }

    fun saveSummaries(summaries: SparseArray<Summary>) {
        val summariesList = ArrayList<Summary>()
        summaries.forEach { key, value ->
            summariesList.add(value)
        }
        Executors.newSingleThreadExecutor().execute {
            dao.insertSummaries(summariesList)
        }
    }

    fun getSummaries(): LiveData<Array<DailySummary>> {
        return dao.getDailySummaries()
    }
}
