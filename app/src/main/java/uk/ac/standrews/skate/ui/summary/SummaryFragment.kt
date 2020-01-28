package uk.ac.standrews.skate.ui.summary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R

class SummaryFragment : Fragment() {

    private lateinit var summaryViewModel: SummaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        summaryViewModel =
            ViewModelProviders.of(this).get(SummaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_summary, container, false)
        val formGrid: GridLayout = root.findViewById(R.id.form_grid)
        val speciesArray = summaryViewModel.getSpecies()
        var index = 0
        Log.e("INDEX", index.toString())
        speciesArray.forEach {
            val label = TextView(context)
            label.setText(it.name)
            formGrid.addView(label, index)
            index++
            val field = EditText(context)
            formGrid.addView(field, index)
            index++
        }
        Log.e("INDEX", index.toString())

        return root
    }
}
