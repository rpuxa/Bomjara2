package ru.rpuxa.bomjara.refactor.v.fragments

import android.view.View
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.chain.view.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.changeVisibility
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.v
import kotlin.reflect.KProperty0

abstract class ChainFragment : CacheFragment() {

    protected fun View.install(name0: String,
                               icon0: Int,
                               changeText: String,
                               elements: List<ChainElement>,
                               ref: KProperty0<MutableLiveData<Int>>) {
        name.text = name0
        icon.setImageResource(icon0)
        val liveDataValue = ref.get().v
        current.text = elements[liveDataValue].name
        if (liveDataValue + 1 >= elements.size) {
            changeVisibility(View.GONE, next, change, change_label, cost, currency)
            return
        }
        val element = elements[liveDataValue + 1]
        val possessions = element.neededPossessions
        val money = element.cost.inv()
        val course = element.courseId
        changeVisibility(View.VISIBLE, next, change, change_label, cost, currency)
        next.text = element.name
        change.text = changeText
        cost.text = money.toString()
        currency.setImageBitmap(money.currency.getIcon(context))
        val viewModel = getViewModel<PlayerViewModel>()
        change.setOnClickListener {
            if (viewModel.doingAction)
                return@setOnClickListener
            val msg =  when {
                viewModel.location.v < possessions.location -> "локация - ${viewModel.getLocation(possessions.location).name}"
                viewModel.transport.v < possessions.transport ->  "транспорт - ${viewModel.getTransport(possessions.transport).name}"
                viewModel.friend.v < possessions.friend -> "кореш - ${viewModel.getFriend(possessions.friend).name}"
                viewModel.home.v < possessions.home -> "дом - ${viewModel.getHome(possessions.home).name}"
                else -> null
            }
            if (msg != null) {
                toast("Требуется $msg")
                return@setOnClickListener
            }

            if (course != -1 && viewModel.coursesProgress.v[course] < viewModel.getCourse(course).length) {
                toast("Требуется пройти курс - ${viewModel.getCourse(course).name}")
                return@setOnClickListener
            }
            if (!viewModel.addMoney(money)) {
                toast(getString(R.string.money_needed))
                return@setOnClickListener
            }

            ref.get().value = liveDataValue + 1
            toast("Выполнено!")
            install(name0, icon0, changeText, elements, ref)
        }
    }
}