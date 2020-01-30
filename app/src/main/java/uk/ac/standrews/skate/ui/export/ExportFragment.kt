package uk.ac.standrews.skate.ui.export

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R

class ExportFragment : Fragment() {

    private lateinit var exportViewModel: ExportViewModel
    private lateinit var exportButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exportViewModel =
            ViewModelProviders.of(this)
                .get(ExportViewModel(this.activity!!.application)::class.java)
        val root = inflater.inflate(R.layout.fragment_export, container, false)
        exportButton = root.findViewById(R.id.export_button)
        exportButton.setOnClickListener {
            doExport()
        }
        return root
    }

    private val REQUESTCODE = 569

    private fun doExport() {
        if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.context, exportViewModel.exportData(), Toast.LENGTH_LONG).show()
        }
        else {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUESTCODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUESTCODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                doExport()
            }
        }
    }

}