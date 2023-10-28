package com.example.goclass.module

import com.example.goclass.Repository
import org.koin.dsl.module

val repositoryModule =
    module {
    single {
        Repository(get())
    }
}
