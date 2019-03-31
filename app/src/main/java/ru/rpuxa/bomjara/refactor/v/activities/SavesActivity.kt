package ru.rpuxa.bomjara.refactor.v.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_save.*
import kotlinx.android.synthetic.main.save_card_view.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import ru.rpuxa.bomjara.refactor.vm.SavesViewModel
import ru.rpuxa.bomjara.utils.ageToString
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.utils.random


class SavesActivity : AppCompatActivity() {

    private val names by lazy { resources.getStringArray(R.array.homeless_names) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = SavesAdapter()
        recycler.adapter = adapter

        val viewModel = getViewModel<SavesViewModel>()

        viewModel.saves.observe(this) {
            adapter.submitList(it)
        }

        new_save.setOnClickListener {
            pickNameDialog(randName) { name ->
                val id = viewModel.newSave(name)
                startGame(id)
            }
        }
        if (intent?.extras?.get("new") == true) {
            new_save.performClick()
        }
    }

    private fun pickNameDialog(startName: String, onName: (String) -> Unit) {
        val nameEditText = EditText(this).apply {
            setText(startName)
            hint = context.getString(R.string.name)
            setSelection(text.length)
        }
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.enter_name))
                .setView(nameEditText)
                .setPositiveButton(getString(R.string.ok), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = nameEditText.text.toString()
            if (name.length < 4) {
                toast(getString(R.string.name_error))
                return@setOnClickListener
            }
            onName(name)
            dialog.dismiss()
        }
    }

    private fun startGame(id: Long) {
        getViewModel<SavesViewModel>().setLastSaveId(id)
        startActivity<ContentActivity>()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_in, R.anim.rigth_out)
    }

    inner class SavesAdapter : ListAdapter<MyDataBase.Save, SaveViewHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.save_card_view, parent, false)
            return SaveViewHolder(view)
        }

        override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
            val save = getItem(position)
            holder.saveName.text = save.name
            holder.age.text = ageToString(save.age)
            holder.money.text = save.rubles.toString()

            val popup = PopupMenu(holder.view.context, holder.view)
            popup.inflate(R.menu.save_menu)

            holder.view.setOnLongClickListener {
                popup.show()
                true
            }

            holder.view.setOnClickListener {
                startGame(save.id)
            }

            val viewModel = getViewModel<SavesViewModel>()

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.rename -> {
                        pickNameDialog(save.name) { newName ->
                            viewModel.renameSave(save.id, newName)
                            toast(getString(R.string.renamed))
                        }
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(holder.view.context)
                                .setTitle(getString(R.string.delete_save))
                                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                                   viewModel.deleteSave(save.id)
                                    toast(getString(R.string.deleted))
                                }
                                .setNegativeButton(getString(R.string.cancel), null)
                                .show()
                    }
                }

                true
            }
        }

    }

    class SaveViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val saveName = view.name!!
        val age = view.age!!
        val money = view.money!!
    }

    object Diff : DiffUtil.ItemCallback<MyDataBase.Save>() {
        override fun areItemsTheSame(oldItem: MyDataBase.Save, newItem: MyDataBase.Save) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MyDataBase.Save, newItem: MyDataBase.Save): Boolean {
            return oldItem.name == newItem.name && oldItem.age == newItem.age && oldItem.rubles == newItem.rubles
        }
    }

    private val randName get() = "Бомж " + names[random.nextInt(names.size)]
}
