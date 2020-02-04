package uk.ac.standrews.skate.ui.flapperSkate

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.ac.standrews.skate.R
import java.nio.file.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

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
            val timestamp = flapperSkateViewModel.saveIndividual(length, width, sex, tagNum)
            lengthField.text.clear()
            widthField.text.clear()
            tagNumField.text.clear()
            PhotoDialogFragment(timestamp).show(fragmentManager!!, "photo")
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

    class PhotoDialogFragment constructor(ts: Long) : DialogFragment() {

        private val timestamp = ts

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Would you like to take a(nother) photo?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        dispatchTakePictureIntent()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        val REQUEST_IMAGE_CAPTURE = 5986

        private fun dispatchTakePictureIntent() {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    }
                    catch (ex: IOException) {
                       null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "uk.ac.standrews.skate.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        PhotoDialogFragment(timestamp).show(fragmentManager!!, "photo")
                    }
                }
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
}
