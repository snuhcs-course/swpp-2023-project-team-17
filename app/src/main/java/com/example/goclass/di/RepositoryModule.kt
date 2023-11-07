package com.example.goclass.di

import com.example.goclass.repository.Repository
import org.koin.dsl.module

val repositoryModule =
    module {
        single {
            Repository(get())
        }
    }
