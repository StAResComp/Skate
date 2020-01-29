package uk.ac.standrews.skate.ui.summary

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R
import uk.ac.standrews.skate.db.entities.Summary
import java.util.*
import androidx.lifecycle.Observer

class SummaryFragment : Fragment() {

    private lateinit var summaryViewModel: SummaryViewModel
    private val SPECIESTAG = R.string.summary_species_tag
    private val FIELDTYPETAG = R.string.summary_field_type_tag
    private val NUMTYPE = 654
    private val NAMETYPE = 655
    private lateinit var saveButton: Button
    private lateinit var formToggleButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        summaryViewModel =
            ViewModelProviders.of(this).get(SummaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_summary, container, false)
        val formGrid: GridLayout = root.findViewById(R.id.form_grid)
        val list: ListView = root.findViewById(R.id.summary_list)
        summaryViewModel.getSummaries().observe(this, Observer {
            list.adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, android.R.id.text1, it)
        })
        val speciesArray = summaryViewModel.getSpecies()
        var index = 0
        speciesArray.forEach {
            val label = View.inflate(context, R.layout.summary_form_label, null) as TextView
            label.text = it.name
            formGrid.addView(label, index)
            index++
            if (it.name.toLowerCase().startsWith("other")) {
                val numField = View.inflate(context, R.layout.summary_form_field_other_num, null) as EditText
                numField.setTag(SPECIESTAG, it.id)
                numField.setTag(FIELDTYPETAG, NUMTYPE)
                formGrid.addView(numField, index)
                index++
                val nameField = View.inflate(context, R.layout.summary_form_field_other_name, null) as EditText
                nameField.setTag(SPECIESTAG, it.id)
                nameField.setTag(FIELDTYPETAG, NAMETYPE)
                formGrid.addView(nameField, index)
                index++
            }
            else {
                val field =
                    View.inflate(context, R.layout.summary_form_field_standard, null) as EditText
                val params = GridLayout.LayoutParams()
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2)
                formGrid.addView(field, index)
                field.layoutParams = params
                field.setTag(SPECIESTAG, it.id)
                field.setTag(FIELDTYPETAG, NUMTYPE)
                index++
            }
        }
        saveButton = root.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val summaryMap = SparseArray<Summary>()
            val now = Date()
            formGrid.children.forEach {
                if (it is EditText && it.text.isNotBlank()) {
                    val speciesId = it.getTag(SPECIESTAG) as Int
                    if (it.getTag(FIELDTYPETAG) == NUMTYPE) {
                        val num = Integer.parseInt(it.text.toString())
                        if (summaryMap.containsKey(speciesId)) {
                            summaryMap[speciesId]!!.number = num
                        }
                        else {
                            val summary = Summary(0, speciesId, null, num, now, now, now)
                            summaryMap[speciesId] = summary
                        }
                    }
                    else if (it.getTag(FIELDTYPETAG) == NAMETYPE) {
                        val otherName = it.text.toString()
                        if (summaryMap.containsKey(speciesId)) {
                            summaryMap[speciesId]!!.otherName = otherName
                        }
                        else {
                            val summary = Summary(0, speciesId, otherName, null, now, now, now)
                            summaryMap[speciesId] = summary
                        }
                    }
                    it.text.clear()
                }
            }
            summaryViewModel.saveSummaries(summaryMap)
        }
        formToggleButton = root.findViewById(R.id.form_toggle_button)
        formToggleButton.setOnClickListener {
            if (formGrid.visibility == View.VISIBLE) {
                formGrid.visibility = View.GONE
                formToggleButton.text = "Show form"
            }
            else {
                formGrid.visibility = View.VISIBLE
                formToggleButton.text = "Hide form"
            }
        }
        return root
    }
}
