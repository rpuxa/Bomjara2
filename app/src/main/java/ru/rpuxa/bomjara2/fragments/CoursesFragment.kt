package ru.rpuxa.bomjara2.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.course.view.*
import kotlinx.android.synthetic.main.courses.view.*
import kotlinx.android.synthetic.main.processed_course.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.actions.Course
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.getCurrencyIcon
import ru.rpuxa.bomjara2.toast

class CoursesFragment : CacheFragment() {

    override val layout = R.layout.courses
    val currentAdapter = CurrentCoursesAdapter(Actions.COURSES)
    val completeAdapter = CompletedCoursesAdapter(Actions.COURSES)

    override fun onChange(view: View) {
        view.completed_courses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = completeAdapter
        }
        view.available_curses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AvailableCoursesAdapter(Actions.COURSES)
        }
        view.current_courses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }
    }

    inner class AvailableCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<AvailableCoursesAdapter.AvailableCoursesHolder>() {
        private val courses = list.filter { Player.CURRENT.courses[it.id] == 0 } as MutableList<Course>

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
                    if (Player.CURRENT.add(course.money)) {
                        Player.CURRENT.courses[courses[position].id]++
                        val adapterPosition = holder.adapterPosition
                        currentAdapter.courses.add(0, courses.removeAt(adapterPosition))
                        notifyItemRemoved(adapterPosition)
                        currentAdapter.notifyItemInserted(0)
                    } else
                        toast(getString(R.string.money_needed))
                }
            }
        }
    }

    inner class CurrentCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<CurrentCoursesAdapter.CurrentCoursesHolder>() {
        val courses = list.filter { Player.CURRENT.courses[it.id] in 1..(it.length - 1) } as MutableList<Course>


        inner class CurrentCoursesHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val name = view.course_name!!
            val process = view.process!!
            val max = view.max!!
            val bar = view.progressBar!!
            val button = view.learning!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentCoursesHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.processed_course, parent, false)

            return CurrentCoursesHolder(view)
        }

        override fun getItemCount() = courses.size

        override fun onBindViewHolder(holder: CurrentCoursesHolder, position: Int) {
            val course = courses[position]
            holder.name.text = course.name
            val p = Player.CURRENT.courses[course.id]
            holder.process.text = p.toString()
            holder.max.text = course.length.toString()

            holder.bar.progress = p
            holder.bar.max = course.length

            holder.button.setOnClickListener {
                Player.CURRENT.courses[course.id]++
                if (Player.CURRENT.courses[course.id] == course.length) {
                    completeAdapter.courses.add(0, courses.removeAt(holder.adapterPosition))
                    notifyItemRemoved(holder.adapterPosition)
                    completeAdapter.notifyItemInserted(0)
                } else {
                    notifyItemChanged(holder.adapterPosition)
                }
                Player.CURRENT += Condition(-5, -5, -5)
            }
        }
    }

    inner class CompletedCoursesAdapter(list: Array<Course>) : RecyclerView.Adapter<CompletedCoursesAdapter.CompletedCoursesHolder>() {

        val courses = list.filter { Player.CURRENT.courses[it.id] >= it.length } as MutableList<Course>

        inner class CompletedCoursesHolder(val view: TextView) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                CompletedCoursesHolder(TextView(parent.context))

        override fun getItemCount() = courses.size

        override fun onBindViewHolder(holder: CompletedCoursesHolder, position: Int) {
            holder.view.text = courses[position].name
        }
    }
}