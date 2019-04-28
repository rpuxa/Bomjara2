package ru.rpuxa.bomjara.refactor.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import ru.rpuxa.bomjara.refactor.m.MyDataBase.Save.Companion.ALIVE
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.utils.postValue
import java.util.*
import javax.inject.Inject

class SavesViewModel : ViewModel() {
    val res = Resources()

    val saves = MutableLiveData<List<MyDataBase.Save>>()

    init {
        setListSaves()
    }

    private fun setListSaves() {
        runBlocking {
            saves.postValue = res.myDataBase.getAllSaves()
        }
    }

    fun newSave(name: String): Long {
        val id = Random().nextLong()
        val coursesSize = res.actionsLoader.courses.size
        runBlocking {
            res.myDataBase.updatePlayer(
                    id,
                    name,
                    0,
                    0,
                    50,
                    0,
                    0,
                    0,
                    0,
                    0,
                    100,
                    100,
                    100,
                    100,
                    75,
                    90,
                    90,
                    IntArray(coursesSize),
                    ALIVE
            )
        }
        return id
    }

    fun renameSave(id: Long, newName: String) {
        runBlocking {
            res.myDataBase.renameSave(id, newName)
        }
        setListSaves()
    }

    fun deleteSave(id: Long) {
        runBlocking {
            res.myDataBase.deleteSave(id)
        }
        setListSaves()
    }

    fun setLastSaveId(id: Long) {
        runBlocking {
            res.myDataBase.setLastSaveId(id)
        }
        setListSaves()
    }

    class Resources {
        @Inject
        lateinit var myDataBase: MyDataBase

        @Inject
        lateinit var actionsLoader: ActionsLoader

        init {
            Bomjara.component.inject(this)
        }
    }
}