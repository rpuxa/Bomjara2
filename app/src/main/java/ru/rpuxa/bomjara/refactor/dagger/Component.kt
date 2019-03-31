package ru.rpuxa.bomjara.refactor.dagger

import dagger.Component
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.refactor.v.activities.SplashActivity
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.refactor.vm.SavesViewModel
import ru.rpuxa.bomjara.refactor.vm.SettingsViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [Provider::class])
interface Component {
    fun inject(playerViewModel: PlayerViewModel.Resources)
    fun inject(playerViewModel: SettingsViewModel.Resources)
    fun inject(actionsLoader: ActionsLoader)
    fun inject(splashActivity: SplashActivity)
    fun inject(resources: SavesViewModel.Resources)
    fun inject(bomjara: Bomjara)
}