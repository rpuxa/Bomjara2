package ru.rpuxa.bomjara2

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.rpuxa.bomjara.refactor.m.MyDataBase


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    lateinit var context: Context
    lateinit var dataBase: MyDataBase

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().context
        dataBase = MyDataBase.create(context)
    }

    @Test
    fun lastSaveId() {
        val rand = -7916290162127893
        dataBase.setLastSaveId(rand)

        assertEquals(rand, dataBase.getLastSaveId())
    }
}
