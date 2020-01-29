package uk.ac.standrews.skate.ui.flapperSkate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R

class FlapperSkateFragment : Fragment() {

    private lateinit var flapperSkateViewModel: FlapperSkateViewModel
    private lateinit var sexSpinner: Spinner
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flapperSkateViewModel =
            ViewModelProviders.of(this).get(FlapperSkateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_flapper_skate, container, false)
        sexSpinner = root.findViewById(R.id.sex)
        sexSpinner.adapter = ArrayAdapter(
            this.context,
            android.R.layout.simple_spinner_dropdown_item,
            android.R.id.text1,
            arrayOf("Male", "Female")
        )
        saveButton = root.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val lengthField = root.findViewById(R.id.length) as EditText
            val length = lengthField.text.toString().toDouble()
            val widthField = root.findViewById(R.id.width) as EditText
            val width = widthField.text.toString().toDouble()
            val sex = (sexSpinner.selectedItem as String)[0]
            val tagNumField = root.findViewById(R.id.tag_num) as EditText
            val tagNum = tagNumField.text.toString()
            flapperSkateViewModel.saveIndividual(length, width, sex, tagNum)
            lengthField.text.clear()
            widthField.text.clear()
            tagNumField.text.clear()
        }
        val list: ListView = root.findViewById(R.id.individuals_list)
        flapperSkateViewModel.getIndividuals().observe(this, Observer {
            list.adapter = ArrayAdapter(
                this.context,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                it
            )
        })
        return root
    }
}
