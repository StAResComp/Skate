package uk.ac.standrews.skate.ui.flapperSkate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.ac.standrews.skate.db.SkateDatabase
import uk.ac.standrews.skate.db.entities.Individual
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class FlapperSkateViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()
    private var flapperSkateId = 0

    fun getIndividuals(): LiveData<Array<Individual>> {
        return dao.getIndividuals()
    }

    fun saveIndividual(length: Double, width: Double, sex: Char, tagNum: String) {
        if (flapperSkateId == 0) {
            val c = Callable {
                dao.getFlapperSkateId()
            }
            flapperSkateId = Executors.newSingleThreadExecutor().submit(c).get()
        }
        val now = Date()
        val individual = Individual(0, flapperSkateId, now, length, width, sex, tagNum, now, now)
        Executors.newSingleThreadExecutor().execute {
            dao.insertIndividual(individual)
        }
    }
}
