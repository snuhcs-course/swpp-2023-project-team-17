package com.example.goclass.class_ui.Chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChattingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Chatting Channel"
    }
    val text: LiveData<String> = _text
}