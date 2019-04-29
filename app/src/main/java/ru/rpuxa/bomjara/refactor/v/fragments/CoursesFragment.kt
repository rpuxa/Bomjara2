package ru.rpuxa.bomjara.refactor.v.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course.view.*
import kotlinx.android.synthetic.main.courses.*
import kotlinx.android.synthetic.main.processed_course.view.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.Course
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe

class CoursesFragment : CacheFragment() {

    override val layout = R.layout.courses

    override fun onChange(view: View) {
        val viewModel = getViewModel<PlayerViewModel>()

       available_curses.layoutManager = LinearLayoutManager(context)
        val availableAdapter = AvailableCoursesAdapter()
        available_curses.adapter = availableAdapter
        viewModel.availableCourses.observe(this) {
            if (it.isEmpty()) {
                available_courses_card_view.visibility = View.GONE
            } else {
                available_courses_card_view.visibility = View.VISIBLE
                availableAdapter.submitList(it)
            }
        }

        current_courses.layoutManager = LinearLayoutManager(context)
        val currentAdapter = CurrentCoursesAdapter()
        current_courses. adapter = currentAdapter
        viewModel.currentCourses.observe(this) {
            if (it.isEmpty()) {
                current_courses_card_view.visibility = View.GONE
            } else {
                current_courses_card_view.visibility = View.VISIBLE
                currentAdapter.submitList(it)
            }
        }


        completed_courses.layoutManager = LinearLayoutManager(context)
        val completeAdapter = CompletedCoursesAdapter()
        completed_courses.adapter = completeAdapter
        viewModel.completedCourses.observe(this) {
            if (it.isEmpty()) {
                completed_courses_card_view.visibility = View.GONE
            } else {
                completed_courses_card_view.visibility = View.VISIBLE
                completeAdapter.submitList(it)
            }
        }

        TipFragment.bind(this, R.id.tip_courses)
    }

    private inner class AvailableCoursesAdapter : ListAdapter<Course, AvailableCoursesHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableCoursesHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.course, parent, false)
            return AvailableCoursesHolder(view)
        }

        override fun onBindViewHolder(holder: AvailableCoursesHolder, position: Int) {
            val viewModel = getViewModel<PlayerViewModel>()
            val course = getItem(position)

            holder.apply {
                name.text = course.name
                cost.text = course.cost.toString()
                currency.setImageBitmap(course.cost.currency.getIcon(context))
                learn.setOnClickListener {
                    if (!viewModel.startStudyCourse(course.id))
                        toast(getString(R.string.money_needed))
                }
            }
        }
    }

    private class AvailableCoursesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name!!
        val learn = view.learn!!
        val cost = view.cost!!
        val currency = view.currency!!
    }

    private object Diff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Course, newItem: Course) =
                oldItem.length == newItem.length
    }

   private inner class CurrentCoursesAdapter : ListAdapter<Course, CurrentCoursesHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentCoursesHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.processed_course, parent, false)
            return CurrentCoursesHolder(view)
        }

        override fun onBindViewHolder(holder: CurrentCoursesHolder, position: Int) {
            val viewModel = getViewModel<PlayerViewModel>()
            val course = getItem(position)
            holder.name.text = course.name
            holder.max.text = course.length.toString()
            holder.skipCurrency.setImageBitmap(course.cost.currency.getIcon(context))
            holder.bar.max = course.length

            holder.learn.setOnClickListener {
                if (viewModel.studyCourse(course.id))
                    toast("Курс пройден!")
            }

            holder.skip.setOnClickListener {
                viewModel.buyCourse(course.id)
            }

            viewModel.coursesProgress.observe(this@CoursesFragment) {
                val progress = it[course.id]
                if (progress in 1 until course.length) {
                    holder.process.text = progress.toString()
                    holder.bar.progress = progress
                    holder.skipCost.text = (course.skipCost.count - course.skipCost.count * progress / course.length).toString()
                }
            }
        }
    }

    private class CurrentCoursesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.course_name
        val process: TextView = view.process
        val max: TextView = view.max
        val bar: ProgressBar = view.progressBar
        val learn: Button = view.learning
        val skip: Button = view.skip_course
        val skipCost: TextView = view.curse_skip_cost
        val skipCurrency: ImageView = view.course_skip_currency
    }

    private inner class CompletedCoursesAdapter : ListAdapter<Course, CompletedCoursesHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                CompletedCoursesHolder(TextView(parent.context))

        override fun onBindViewHolder(holder: CompletedCoursesHolder, position: Int) {
            holder.view.text = getItem(position).name
        }
    }

    private class CompletedCoursesHolder(val view: TextView) : RecyclerView.ViewHolder(view)
}