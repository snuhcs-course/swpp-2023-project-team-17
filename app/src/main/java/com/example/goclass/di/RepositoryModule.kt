package com.example.goclass.di

import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.ChatRepository
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single {
            AttendanceRepository(get())
        }
        single {
            ChatRepository(get())
        }
        single {
            ClassRepository(get())
        }
        single {
            UserRepository(get())
        }
    }
