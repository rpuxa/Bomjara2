package ru.rpuxa.bomjara2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.save_card_view.view.*
import kotlinx.android.synthetic.main.saves_list.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.save.Save

class SavesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.saves_list, container)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.recycler
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = SavesAdapter(listOf(Save("123", 123, 1, 89125123)))
    }


    class SavesAdapter(private val list: List<Save>) : RecyclerView.Adapter<SavesAdapter.SaveViewHolder>() {
        class SaveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val saveName = view.name!!
            val age = view.age!!
            val money = view.money!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.save_card_view, parent)
            view.delete.setOnClickListener {

            }

            view.rename.setOnClickListener {

            }

            return SaveViewHolder(view)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
            val save = list[position]
            holder.saveName.text = save.name
            holder.age.text = "${save.ageYear} лет ${save.ageDays} дней"
            holder.money.text = save.money.toString()
        }
    }
}