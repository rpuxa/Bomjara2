package ru.rpuxa.bomjara.refactor.v.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.tip.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.refactor.vm.SettingsViewModel
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.R.id as i

class TipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tip, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = getViewModel<SettingsViewModel>()
        viewModel.showTips.observe(this) { show ->
            view.visibility = if (show) View.VISIBLE else View.GONE
        }
        close_tip.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle("Убрать подсказки?")
                    .setMessage("Включить их можно будет в настройках")
                    .setPositiveButton("Убрать") { _, _ ->
                        viewModel.setShowTips(false)
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
        }
    }

    fun setMenu(id: Int) {
        val text = when (id) {
            ActionsMenus.ENERGY.id -> "Бодрость сильно влияет на ваш заработок, чем выше бодрость тем лучше"
            ActionsMenus.FOOD.id -> "Поддерживайте уровень еды, чтобы не умереть с голоду!"
            ActionsMenus.HEALTH.id -> "Здоровье превыше всего! Не дайте ему опуститься ниже нуля, иначе смерти не миновать!"
            ActionsMenus.JOBS.id -> "Работа - основной заработок в игре. Её эффективность сильно зависит от уровня бодрости. " +
                    "Так же как и все действия делится на легальную и нет. " +
                    "Если работа нелегальна, то есть высокий шанс, что вас поймают. Будьте осторожны!"
            i.tip_location -> "Продвигайтесь по локациям, чтобы открывать новые возможности. " +
                    "А кореша помогут вам найти более лучшую работу"
            i.tip_transport -> "Покупай дома и транспорт, чтобы переходить на новые локации"
            i.tip_courses -> "Здесь вы можете проходить разные курсы, чтобы открыть корешей, транспорт и т.п."
            /* i.tip_vip -> "Здесь вы можете купить уникальные товары за особую валюту - алмазы. " +
                     "Они могут попадаться, когда вы совершаете какое либо действие." +
                     "Либо вы можете их получить, посмотрев рекламу"*/
            else -> throw IllegalStateException("unknown tip")
        }
        text_tip.text = text
    }

    companion object {
        fun bind(fragment: Fragment, @IdRes id: Int, menu: Int = id) {
            (fragment.childFragmentManager.findFragmentById(id) as TipFragment).setMenu(menu)
        }
    }
}