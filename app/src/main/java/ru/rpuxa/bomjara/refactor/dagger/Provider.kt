package ru.rpuxa.bomjara.refactor.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import javax.inject.Singleton

@Module
class Provider(val app: Bomjara) {

    @Provides
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideMyDataBase() = MyDataBase.create(provideContext())

    @Provides
    @Singleton
    fun actionLoader() = ActionsLoader()
}