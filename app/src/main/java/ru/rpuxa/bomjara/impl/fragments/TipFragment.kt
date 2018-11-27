package ru.rpuxa.bomjara.impl.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tip.view.*
import ru.rpuxa.bomjara.CurrentData.settings
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.R.id as i

class TipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tip, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTip(view, id)
    }

    companion object {
        val allTips = HashSet<View>()

        fun setTip(view: View, id: Int) {
            if (!settings.showTips) {
                view.visibility = View.GONE
                return
            }
            if (view.text_tip.text.isNotBlank())
                return

            view.visibility = View.VISIBLE
            allTips.add(view)

            view.text_tip.text = when (id) {
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
                i.tip_vip -> "Здесь вы можете купить уникальные товары за особую валюту - алмазы. " +
                        "Они могут попадаться, когда вы совершаете какое либо действие." +
                        "Либо вы можете их получить, посмотрев рекламу"
                else -> throw IllegalStateException("unknown tip")
            }

            view.close_tip.setOnClickListener {
                AlertDialog.Builder(view.context)
                        .setTitle("Убрать подсказки?")
                        .setMessage("Включить их можно будет в настройках")
                        .setPositiveButton("Убрать") { _, _ ->
                            settings.showTips = false
                            allTips.forEach { tip -> tip.visibility = View.GONE }
                        }
                        .setNegativeButton("Отмена", null)
                        .show()
            }
        }
    }
}