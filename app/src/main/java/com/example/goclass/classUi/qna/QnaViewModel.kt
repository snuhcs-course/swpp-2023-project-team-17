package com.example.goclass.classUi.qna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QnaViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
            value = "This is Q&A Channel"
    }
    val text: LiveData<String> = _text
}
