package ru.rpuxa.bomjara.refactor.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.utils.postValue
import javax.inject.Inject

class SettingsViewModel : ViewModel() {

    private val res = Resources()

    val showTips = MutableLiveData<Boolean>()


    init {
        runBlocking {
            showTips.postValue = res.myDataBase.getShowTips()
        }
    }

    fun setShowTips(bFlag: Boolean) {
        showTips.value = bFlag
        GlobalScope.launch {
            res.myDataBase.setShowTips(bFlag)
        }
    }

    fun isSavesIsEmpty(): Boolean = runBlocking { res.myDataBase.getLastSaveId() == 0L }

    class Resources {
        @Inject
        lateinit var myDataBase: MyDataBase

        init {
            Bomjara.component.inject(this)
        }
    }
}