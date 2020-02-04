package uk.ac.standrews.skate.ui.flapperSkate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.standrews.skate.db.SkateDatabase
import uk.ac.standrews.skate.db.entities.Individual
import uk.ac.standrews.skate.db.entities.Photo
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class FlapperSkateViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()
    private var flapperSkateId = 0

    fun getIndividuals(): LiveData<Array<Individual>> {
        val c = Callable {
            dao.getIndividuals()
        }
        return Executors.newSingleThreadExecutor().submit(c).get()
    }

    fun saveIndividual(length: Double, width: Double, sex: Char, tagNum: String): Pair<Long, Long> {
        if (flapperSkateId == 0) {
            val c = Callable {
                dao.getFlapperSkateId()
            }
            flapperSkateId = Executors.newSingleThreadExecutor().submit(c).get()
        }
        val now = Date()
        val individual = Individual(0, flapperSkateId, now, length, width, sex, tagNum, now, now)
        val c = Callable {
            dao.insertIndividual(individual)
        }
        val individualId = Executors.newSingleThreadExecutor().submit(c).get()
        return Pair(individualId, now.time)
    }

    fun savePhotos(individualId: Long, photoPaths: List<String>) {
        val now = Date()
        val photos = ArrayList<Photo>()
        photoPaths.forEach {
            photos.add(Photo(0, individualId.toInt(), it, now, now))
        }
        Executors.newSingleThreadExecutor().execute {
            dao.insertPhotos(photos)
        }
    }
}
