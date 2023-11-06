package com.example.goclass.DIModules

import com.example.goclass.UI.ClassUI.Attendances.Professor.ProfessorAttendanceListViewModel
import com.example.goclass.UI.ClassUI.Attendances.Professor.ProfessorAttendanceViewModel
import com.example.goclass.UI.ClassUI.Attendances.Student.StudentAttendanceViewModel
import com.example.goclass.UI.MainUI.Login.LoginViewModel
import com.example.goclass.UI.MainUI.UserMain.ProfessorMainViewModel
import com.example.goclass.UI.MainUI.Profile.ProfileViewModel
import com.example.goclass.UI.MainUI.UserMain.StudentMainViewModel
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
