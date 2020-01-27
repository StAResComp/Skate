package uk.ac.standrews.skate.ui.flapperSkate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R

class FlapperSkateFragment : Fragment() {

    private lateinit var flapperSkateViewModel: FlapperSkateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flapperSkateViewModel =
            ViewModelProviders.of(this).get(FlapperSkateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_flapper_skate, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        flapperSkateViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
