package uk.ac.standrews.skate.ui.export

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
            exportViewModel.exportData()
        }
        return root
    }

}