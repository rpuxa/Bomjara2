package ru.rpuxa.bomjara.impl.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.course.view.*
import kotlinx.android.synthetic.main.courses.*
import kotlinx.android.synthetic.main.courses.view.*
import kotlinx.android.synthetic.main.processed_course.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.actions.DefaultCourse
import ru.rpuxa.bomjara.impl.divider
import ru.rpuxa.bomjara.impl.getCurrencyIcon
import ru.rpuxa.bomjara.impl.player.DefaultCondition
import ru.rpuxa.bomjara.impl.save
import ru.rpuxa.bomjara.impl.toast

class CoursesFragment : CacheFragment() {

    override val layout = R.layout.courses
    val currentAdapter = CurrentCoursesAdapter(actionsBase.courses)
    val completeAdapter = CompletedCoursesAdapter(actionsBase.courses)

    override fun onChange(view: View) {

        view.completed_courses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = completeAdapter
        }
        view.available_curses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AvailableCoursesAdapter(actionsBase.courses)
        }
        view.current_courses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }

        if (completeAdapter.courses.isEmpty())
            completed_courses_card_view.visibility = View.GONE

        if (currentAdapter.courses.isEmpty())
            current_courses_card_view.visibility = View.GONE
    }

    inner class AvailableCoursesAdapter(val list: Array<DefaultCourse>) : RecyclerView.Adapter<AvailableCoursesAdapter.AvailableCoursesHolder>() {
        private val courses = list.filter { player.courses[it.id] == 0 } as MutableList<DefaultCourse>

        inner class AvailableCoursesHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val name = view.name!!
            val learn = view.learn!!
            val cost = view.cost!!
            val currency = view.currency!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableCoursesHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.course, parent, false)
            return AvailableCoursesHolder(view)
        }

        override fun getItemCount() =
                courses.size

        override fun onBindViewHolder(holder: AvailableCoursesHolder, position: Int) {
            val course = courses[holder.adapterPosition]

            holder.apply {
                name.text = course.name
                cost.text = course.cost.toString()
                currency.setImageBitmap(context.getCurrencyIcon(course.cost))
                learn.setOnClickListener {
                    if (player.addMoney(course.cost)) {
                        val id = courses[holder.adapterPosition].id
                        player.courses[id]++
                        currentAdapter.courses.add(0, courses.removeAt(holder.adapterPosition))
                        notifyItemRemoved(holder.adapterPosition)
                        currentAdapter.notifyItemInserted(0)
                        current_courses_card_view.visibility = View.VISIBLE
                    } else
                        toast(getString(R.string.money_needed))
                }
            }
        }
    }

    inner class CurrentCoursesAdapter(list: Array<DefaultCourse>) : RecyclerView.Adapter<CurrentCoursesAdapter.CurrentCoursesHolder>() {
        val courses = list.filter { player.courses[it.id] in 1..(it.length - 1) } as MutableList<DefaultCourse>


        inner class CurrentCoursesHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val name = view.course_name!!
            val process = view.process!!
            val max = view.max!!
            val bar = view.progressBar!!
            val button = view.learning!!
            val skip = view.skip_course
            val skipCost = view.curse_skip_cost
            val skipCurrency = view.course_skip_currency
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentCoursesHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.processed_course, parent, false)

            return CurrentCoursesHolder(view)
        }

        override fun getItemCount() = courses.size

        override fun onBindViewHolder(holder: CurrentCoursesHolder, position: Int) {
            val course = courses[position]
            fun cost() = course.skipCost.multiply(1 - player.courses[course.id].toDouble() / course.length)
            fun updateCost() {
                holder.skipCost.text = cost().count.divider()
            }
            holder.name.text = course.name
            val p = player.courses[course.id]
            holder.process.text = p.toString()
            holder.max.text = course.length.toString()

            holder.bar.progress = p
            holder.bar.max = course.length

            holder.button.setOnClickListener {
                val id = course.id
                player.courses[id]++
                update(course, holder)
                player.addCondition(DefaultCondition(-5, -5, -5))
                updateCost()
            }

            holder.skip.setOnClickListener {
                if (player.addMoney(cost())) {
                    player.courses[course.id] = course.length
                    update(course, holder)
                } else {
                    toast(R.string.money_needed)
                }
            }
            updateCost()

            holder.skipCurrency.setImageBitmap(context.getCurrencyIcon(course.cost))
        }

        private fun update(course: DefaultCourse, holder: CurrentCoursesHolder) {
            if (player.courses[course.id] == course.length) {
                completeAdapter.courses.add(0, courses.removeAt(holder.adapterPosition))
                notifyItemRemoved(holder.adapterPosition)
                completeAdapter.notifyItemInserted(0)
                completed_courses_card_view.visibility = View.VISIBLE
                toast("Курс пройден!")
            } else {
                notifyItemChanged(holder.adapterPosition)
            }
            if (courses.isEmpty())
                current_courses_card_view.visibility = View.GONE
        }
    }

    inner class CompletedCoursesAdapter(list: Array<DefaultCourse>) : RecyclerView.Adapter<CompletedCoursesAdapter.CompletedCoursesHolder>() {

        val courses = list.filter { player.courses[it.id] >= it.length } as MutableList<DefaultCourse>

        inner class CompletedCoursesHolder(val view: TextView) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                CompletedCoursesHolder(TextView(parent.context))

        override fun getItemCount() = courses.size

        override fun onBindViewHolder(holder: CompletedCoursesHolder, position: Int) {
            holder.view.text = courses[position].name
        }
    }

    override fun onPause() {
        context.save()
        super.onPause()
    }
}