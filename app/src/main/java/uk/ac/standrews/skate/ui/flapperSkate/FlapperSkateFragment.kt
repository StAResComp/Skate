package uk.ac.standrews.skate.ui.flapperSkate

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R
import java.io.*
import java.util.*
import kotlin.math.roundToInt

class FlapperSkateFragment : Fragment(), PhotoDialogFragment.PhotoDialogListener {

    private lateinit var flapperSkateViewModel: FlapperSkateViewModel
    private lateinit var sexSpinner: Spinner
    private lateinit var saveButton: Button
    private var timestamp: Long = 0
    private var individualId: Long = 0
    private lateinit var currentPhotoPath: String
    private val photoPaths = ArrayList<String>()
    private lateinit var lengthField: EditText
    private lateinit var widthField: EditText
    private lateinit var weightField: TextView

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
        lengthField = root.findViewById(R.id.length) as EditText
        widthField = root.findViewById(R.id.width) as EditText
        weightField = root.findViewById(R.id.weight) as TextView
        lengthField.setOnFocusChangeListener { _, _ ->
            doWeight()
        }
        widthField.setOnFocusChangeListener { _, _ ->
            doWeight()
        }
        sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                doWeight()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        saveButton = root.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            if (lengthField.text.isNotBlank() && widthField.text.isNotBlank()) {
                val length = lengthField.text.toString().toDouble()
                val width = widthField.text.toString().toDouble()
                val sex = (sexSpinner.selectedItem as String)[0]
                val tagNumField = root.findViewById(R.id.tag_num) as EditText
                val tagNum = tagNumField.text.toString()
                val individualDetails =
                    flapperSkateViewModel.saveIndividual(length, width, sex, tagNum)
                individualId = individualDetails.first
                timestamp = individualDetails.second
                lengthField.text.clear()
                widthField.text.clear()
                tagNumField.text.clear()
                doPhotoDialog()
            }
            else {
                val alertDialog = AlertDialog.Builder(this.context).create()
                alertDialog.setTitle("Warning!")
                alertDialog.setMessage("Both length and width must be entered")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, which ->
                        dialog.dismiss()
                    }
                alertDialog.show()
            }
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

    private fun doWeight() {
        if (lengthField.text.isNotBlank() && widthField.text.isNotBlank()) {
            val l = lengthField.text.toString().toDouble().roundToInt()
            val w = widthField.text.toString().toDouble().roundToInt()
            val s= (sexSpinner.selectedItem as String)[0]
            val weight = flapperSkateViewModel.getWeight(s, l, w)
            if (weight == null) {
                weightField.text = "Not known"
            }
            else {
                weightField.text = weight.toString()
            }
        }
        else {
            weightField.text = ""
        }
    }

    private val REQUESTCODE = 2561

    private fun doPhotoDialog() {
        if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                val photoDialog = PhotoDialogFragment()
                photoDialog.setTargetFragment(this, 300)
                photoDialog.show(fragmentManager!!, "photo")
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
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
                doPhotoDialog()
            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        dispatchTakePictureIntent()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        flapperSkateViewModel.savePhotos(individualId, photoPaths)
    }

    val REQUEST_IMAGE_CAPTURE = 2

    private fun dispatchTakePictureIntent() {
        val photoFile = createImageFile()
        currentPhotoPath = photoFile.path
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                photoFile.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "uk.ac.standrews.skate.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photoPaths.add(currentPhotoPath)
            Toast.makeText(this.context, "Photo saved to $currentPhotoPath", Toast.LENGTH_LONG).show()
            doPhotoDialog()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val dir = File(downloads, timestamp.toString())
        dir.mkdir()
        // Create an image file name
        return File.createTempFile(
            "image_${Date().time}", /* prefix */
            ".jpg", /* suffix */
            dir /* directory */
        )
    }
}
