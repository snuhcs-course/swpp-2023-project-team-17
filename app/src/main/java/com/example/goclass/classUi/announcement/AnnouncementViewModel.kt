package com.example.goclass.classUi.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnnouncementViewModel : ViewModel() {
    private val _text =
        MutableLiveData<String>()
            .apply {
                value = "This is Announcement Channel"
            }
    val text: LiveData<String> = _text
}
