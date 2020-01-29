package uk.ac.standrews.skate.ui.summary

import android.app.Application
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.ac.standrews.skate.db.SkateDatabase
import uk.ac.standrews.skate.db.entities.Species
import uk.ac.standrews.skate.db.entities.Summary
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class SummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()

    fun getSpecies(): Array<Species> {
        val c = Callable {
            dao.getSpecies()
        }
        return Executors.newSingleThreadExecutor().submit(c).get()
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

}
