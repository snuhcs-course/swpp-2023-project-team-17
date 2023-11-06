package com.example.goclass.DIModules

import com.example.goclass.Repository.Repository
import org.koin.dsl.module

val repositoryModule =
    module {
        single {
            Repository(get())
        }
    }
