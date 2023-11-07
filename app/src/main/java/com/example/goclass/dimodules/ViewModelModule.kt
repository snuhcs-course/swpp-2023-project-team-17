package com.example.goclass.dimodules

import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceListViewModel
import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceViewModel
import com.example.goclass.ui.classui.attendances.student.StudentAttendanceViewModel
import com.example.goclass.ui.mainui.login.LoginViewModel
import com.example.goclass.ui.mainui.usermain.ProfessorMainViewModel
import com.example.goclass.ui.mainui.profile.ProfileViewModel
import com.example.goclass.ui.mainui.usermain.StudentMainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel {
            ProfessorMainViewModel(get())
        }
        viewModel {
            ProfileViewModel(get())
        }
        viewModel {
            StudentMainViewModel(get(), androidApplication())
        }
        viewModel {
            StudentAttendanceViewModel(get())
        }
        viewModel {
            ProfessorAttendanceViewModel(get())
        }
        viewModel {
            ProfessorAttendanceListViewModel(get())
        }
        viewModel {
            LoginViewModel(get())
        }
    }
