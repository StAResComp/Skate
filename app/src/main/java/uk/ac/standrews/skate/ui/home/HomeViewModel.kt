package uk.ac.standrews.skate.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.standrews.skate.db.SkateDatabase
import uk.ac.standrews.skate.db.entities.Effort
import java.util.*
import java.util.concurrent.Executors

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()

    fun getPreviousEffort(): LiveData<Array<Effort>> {
        return dao.getPreviousEffort()
    }

    fun getLastEffort(): LiveData<Effort> {
        return dao.getLastEffort()
    }

    fun startEffort(numRods: Int, lat: Double, lon: Double) {
        val now = Date()
        val effort = Effort(0, now, lat, lon, null, null, null, numRods, now, now)
        Executors.newSingleThreadExecutor().execute {
            dao.insertEffort(effort)
        }
    }

    fun finishEffort(id: Int, lat: Double, lon:Double) {
        val now = Date()
        Executors.newSingleThreadExecutor().execute {
            val effort = dao.getEffortById(id)
            if (effort != null) {
                effort.finishedAt = now
                effort.finishingLatitude = lat
                effort.finishingLongitude = lon
                effort.modifiedAt = now
                dao.updateEffort(effort)
            }
        }
    }
}
