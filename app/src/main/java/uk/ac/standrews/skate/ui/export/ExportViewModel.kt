package uk.ac.standrews.skate.ui.export

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import uk.ac.standrews.skate.db.SkateDatabase

class ExportViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()

    fun exportData() {

    }

}