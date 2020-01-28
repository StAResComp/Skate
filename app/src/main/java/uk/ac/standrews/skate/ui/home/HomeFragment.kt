package uk.ac.standrews.skate.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R
import uk.ac.standrews.skate.db.entities.Effort

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

    private val REQUESTCODE = 568

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE)
                        as LocationManager
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 5f, this
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
        else {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUESTCODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUESTCODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }
}
