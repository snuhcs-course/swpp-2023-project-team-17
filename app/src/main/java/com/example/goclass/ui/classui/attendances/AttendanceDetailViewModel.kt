package com.example.goclass.ui.classui.attendances

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class AttendanceDetailViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    //private val _toastMessage = MutableLiveData<String>()
    //val toastMessage: LiveData<String> get() = _toastMessage
}
