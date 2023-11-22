package com.example.goclass.di

import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceListViewModel
import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceViewModel
import com.example.goclass.ui.classui.attendances.student.StudentAttendanceViewModel
import com.example.goclass.ui.classui.chats.ChatCommentViewModel
import com.example.goclass.ui.classui.chats.ChatViewModel
import com.example.goclass.ui.mainui.login.LoginViewModel
import com.example.goclass.ui.mainui.profile.ProfileViewModel
import com.example.goclass.ui.mainui.usermain.ProfessorMainViewModel
import com.example.goclass.ui.mainui.usermain.StudentMainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel {
            ProfessorMainViewModel(get(), get(), androidApplication())
        }
        viewModel {
            ProfileViewModel(get())
        }
        viewModel {
            StudentMainViewModel(get(), get(), androidApplication())
        }
        viewModel {
            StudentAttendanceViewModel(get(), get())
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
        viewModel {
            ChatViewModel(get())
        }
        viewModel {
            ChatCommentViewModel(get())
        }
    }
