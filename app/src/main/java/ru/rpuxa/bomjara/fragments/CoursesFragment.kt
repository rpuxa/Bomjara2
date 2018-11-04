package ru.rpuxa.bomjara.fragments

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
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.actions.Course
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition

class CoursesFragment : CacheFragment() {

    override val layout = R.layout.courses
    val currentAdapter = CurrentCoursesAdapter(Actions.courses)
    val completeAdapter = CompletedCoursesAdapter(Actions.courses)

    override fun onChange(view: View) {

        view.completed_courses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = completeAdapter
        }
        view.available_curses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AvailableCoursesAdapter(Actions.courses)
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

    inner class AvailableCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<AvailableCoursesAdapter.AvailableCoursesHolder>() {
        private val courses = list.filter { Player.current.courses[it.id] == 0 } as MutableList<Course>

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
            val course = courses[position]

            holder.apply {
                name.text = course.name
                cost.text = course.money.toString()
                currency.setImageBitmap(context.getCurrencyIcon(course.money))
                learn.setOnClickListener {
                    if (Player.current.add(course.money)) {
                        Player.current.courses[courses[position].id]++
                        val adapterPosition = holder.adapterPosition
                        currentAdapter.courses.add(0, courses.removeAt(adapterPosition))
                        notifyItemRemoved(adapterPosition)
                        currentAdapter.notifyItemInserted(0)
                        current_courses_card_view.visibility = View.VISIBLE
                    } else
                        toast(getString(R.string.money_needed))
                }
            }
        }
    }

    inner class CurrentCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<CurrentCoursesAdapter.CurrentCoursesHolder>() {
        val courses = list.filter { Player.current.courses[it.id] in 1..(it.length - 1) } as MutableList<Course>


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
            fun cost() = course.skipCost * (1 - Player.current.courses[course.id].toDouble() / course.length)
            fun updateCost() {
                holder.skipCost.text = cost().count.divider()
            }
            holder.name.text = course.name
            val p = Player.current.courses[course.id]
            holder.process.text = p.toString()
            holder.max.text = course.length.toString()

            holder.bar.progress = p
            holder.bar.max = course.length

            holder.button.setOnClickListener {
                Player.current.courses[course.id]++
                update(course, holder)
                Player.current += Condition(-5, -5, -5)
                updateCost()
            }

            holder.skip.setOnClickListener {
                if (Player.current.add(cost())) {
                    Player.current.courses[course.id] = course.length
                    update(course, holder)
                } else {
                    toast(R.string.money_needed)
                }
            }
            updateCost()

            holder.skipCurrency.setImageBitmap(context.getCurrencyIcon(course.money))
        }

        private fun update(course: Course, holder: CurrentCoursesHolder) {
            if (Player.current.courses[course.id] == course.length) {
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

    inner class CompletedCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<CompletedCoursesAdapter.CompletedCoursesHolder>() {

        val courses = list.filter { Player.current.courses[it.id] >= it.length } as MutableList<Course>

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