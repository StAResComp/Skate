package uk.ac.standrews.skate.ui.export

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import uk.ac.standrews.skate.db.SkateDatabase
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ExportViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()

    fun exportData() {
        val c = Callable {
            Triple(dao.getEffort(), dao.getSummariesForExport(), dao.getIndividualsForExport())
        }
        val data = Executors.newSingleThreadExecutor().submit(c).get()
        val effort = data.first
        var summaries = data.second
        var individuals = data.third
        Log.e("EXPORT", "${effort.size}, ${summaries.size}, ${individuals.size}")
    }

}