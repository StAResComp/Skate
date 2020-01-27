package uk.ac.standrews.skate.ui.flapperSkate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlapperSkateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is flapperSkate Fragment"
    }
    val text: LiveData<String> = _text
}
