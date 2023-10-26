package com.example.goclass.module

import com.example.goclass.mainUi.ProfessorMainViewModel
import com.example.goclass.mainUi.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        ProfessorMainViewModel(get())
    }
    viewModel{
        ProfileViewModel(get())
    }
}