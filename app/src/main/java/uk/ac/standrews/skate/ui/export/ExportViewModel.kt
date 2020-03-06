package uk.ac.standrews.skate.ui.export

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import uk.ac.standrews.skate.db.Exportable
import uk.ac.standrews.skate.db.SkateDatabase
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ExportViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SkateDatabase.getSkateDatabase(getApplication()).skateDao()

    fun exportData(): String {
        val c = Callable {
            Triple(dao.getEffort(), dao.getSummariesForExport(), dao.getIndividualsForExport())
        }
        val data = Executors.newSingleThreadExecutor().submit(c).get()
        val effort = data.first
        val summaries = data.second
        val individuals = data.third
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dirName = "skate-${sdf.format(Date())}"
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dirName)
        dir.mkdir()
        writeToFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$dirName/effort.csv"
            ),
            effort
        )
        writeToFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$dirName/summaries.csv"
            ),
            summaries
        )
        writeToFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$dirName/individuals.csv"
            ),
            individuals
        )
        return "Data exported to ${dir.path}"
    }

    private fun writeToFile(file: File, data: List<Exportable>) {
        if (!data.isEmpty()) {
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(data.first().getCsvHeaderRow())
            bufferedWriter.newLine()
            data.forEach {
                bufferedWriter.write(it.toCsvString())
                bufferedWriter.newLine()
            }
            bufferedWriter.close()
            fileWriter.close()
        }
    }
}
