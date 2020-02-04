package uk.ac.standrews.skate.ui.flapperSkate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PhotoDialogFragment : DialogFragment() {

    internal lateinit var listener: PhotoDialogListener


    interface PhotoDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = targetFragment as PhotoDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement PhotoDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Would you like to take a(nother) photo?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    listener.onDialogPositiveClick(this)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                    listener.onDialogNegativeClick(this)
                    dialog.dismiss()
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}