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
import java.text.DateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class SummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()
    private val summaryStrings: MutableLiveData<Array<String>> by lazy {
        MutableLiveData<Array<String>>()
    }
    private lateinit var species: Array<Species>
    private lateinit var speciesMap: SparseArray<String>
    private val loc = Locale("en", "GB")
    private val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc)

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

    fun getSummaries(): LiveData<Array<String>> {
        if (!::speciesMap.isInitialized) {
            val speciesArr = getSpecies()
            speciesMap = SparseArray(speciesArr.size)
            speciesArr.forEach {
                speciesMap.put(it.id, it.name)
            }
        }
        dao.getSummaries().observeForever(Observer {
            val summaryStringsMap = ArrayMap<Date,String>()
            it.forEach {
                val speciesId = it.speciesId
                if (speciesId != null) {
                    if (!summaryStringsMap.contains(it.date)) {
                        summaryStringsMap[it.date] =
                            "${dateFormat.format(it.date)}: ${it.number} ${speciesMap[speciesId]}"
                    }
                    else {
                        summaryStringsMap[it.date] =
                            summaryStringsMap.get(it.date).plus(", ${it.number} ${speciesMap[speciesId]}")
                    }
                    if (it.otherName != null) {
                        summaryStringsMap[it.date] =
                            summaryStringsMap.get(it.date).plus(" (${it.otherName})")
                    }
                }
            }
            val summaryStringsArray = arrayOfNulls<String>(summaryStringsMap.size)
            summaryStrings.value = ArrayList(summaryStringsMap.values).toArray(summaryStringsArray)
        })
        return summaryStrings
    }
}
