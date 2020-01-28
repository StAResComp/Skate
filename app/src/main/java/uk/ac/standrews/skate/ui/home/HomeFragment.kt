package uk.ac.standrews.skate.ui.home

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R
import uk.ac.standrews.skate.db.entities.Effort
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment(), LocationListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationManager: LocationManager
    private lateinit var startButton: Button
    private lateinit var finishButton: Button
    private lateinit var numRodsField: EditText
    private lateinit var numRodsLabel: TextView
    private lateinit var currentEffortText: TextView
    private var currentEffort: Effort? = null
    private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this)
                .get(HomeViewModel(this.activity!!.application)::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val list: ListView = root.findViewById(R.id.effort_list)
        homeViewModel.getPreviousEffort().observe(this, Observer {
            list.adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, android.R.id.text1, it)
        })
        startButton = root.findViewById(R.id.start_button)
        finishButton = root.findViewById(R.id.finish_button)
        numRodsField = root.findViewById(R.id.num_rods)
        numRodsLabel = root.findViewById(R.id.num_rods_label) as TextView
        currentEffortText = root.findViewById(R.id.current_effort_text) as TextView
        homeViewModel.getLastEffort().observe(this, Observer {
            if (it != null) {
                if (it.finishedAt == null) {
                    Log.e("OBSERVING_CURRENT", "Not null")
                    currentEffort = it
                    currentEffortText.text = it.toString()
                    finishMode()
                }
                else {
                    startMode()
                }
            }
        })
        startButton.setOnClickListener {
            val currentLocation = location
            if (currentLocation != null) {
                homeViewModel.startEffort(
                    Integer.parseInt(numRodsField.text.toString()),
                    currentLocation.latitude, currentLocation.longitude
                )
                finishMode()
            }
        }
        finishButton.setOnClickListener {
            val currentLocation = location
            val effortToFinish = currentEffort
            Log.e("FINISHING", effortToFinish.toString())
            if (currentLocation != null && effortToFinish != null) {
                homeViewModel.finishEffort(
                    effortToFinish.id,
                    currentLocation.latitude, currentLocation.longitude
                )
                startMode()
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun startMode() {
        startButton.visibility = View.VISIBLE
        numRodsField.visibility = View.VISIBLE
        numRodsLabel.visibility = View.VISIBLE
        currentEffortText.visibility = View.GONE
        finishButton.visibility = View.GONE
    }

    private fun finishMode() {
        startButton.visibility = View.GONE
        numRodsField.visibility = View.GONE
        numRodsLabel.visibility = View.GONE
        currentEffortText.visibility = View.VISIBLE
        finishButton.visibility = View.VISIBLE
    }

    private fun getLocation() {
        try {
            locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
        catch(e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
