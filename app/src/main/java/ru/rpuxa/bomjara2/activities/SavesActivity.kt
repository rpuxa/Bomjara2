package ru.rpuxa.bomjara2.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_save.*
import kotlinx.android.synthetic.main.save_card_view.view.*
import ru.rpuxa.bomjara2.*
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.save.Save
import ru.rpuxa.bomjara2.save.SaveLoader
import ru.rpuxa.bomjara2.settings.Settings


class SavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val new = intent?.extras?.get("new")

        setContentView(R.layout.activity_save)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)
        val savesAdapter = SavesAdapter(SaveLoader.saves)
        recycler.adapter = savesAdapter

        new_save.setOnClickListener {
            pickNameDialog { name ->
                startGame(Player(random.nextLong(), name, false))
            }
        }
        if (new != null && new is Boolean && new) {
            new_save.performClick()
        }

    }

    private fun pickNameDialog(startName: String = "", onName: (String) -> Unit) {
        var dialog = null as Dialog?
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Введите имя бомжа")

        val nameEditText = EditText(this)
        nameEditText.setText(startName)
        builder.setView(nameEditText)

        builder.setPositiveButton("Ок") { _, _ ->
            val name = nameEditText.text.toString()
            if (name.length < 4) {
                toast("Имя должно быть не меньше 4 символов")
                return@setPositiveButton
            }
            dialog!!.dismiss()
            onName(name)
        }

        builder.setNegativeButton("Отмена") { _, _ ->
            dialog!!.dismiss()
        }

        dialog = builder.show()
    }

    private fun startGame(player: Player) {
        Player.CURRENT = player
        startActivity<ContentActivity>()
        Settings.lastSave = player.id
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class SavesAdapter(private val list: List<Save>) : RecyclerView.Adapter<SavesAdapter.SaveViewHolder>() {

        inner class SaveViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val saveName = view.name!!
            val age = view.age!!
            val money = view.money!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.save_card_view, parent, false)

            return SaveViewHolder(view)
        }

        override fun getItemCount() = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
            val save = list[position]
            holder.saveName.text = save.name
            holder.age.text = getStringAge(save.age)
            holder.money.text = save.rubles.toString()

            val popup = PopupMenu(holder.view.context, holder.view)
            popup.inflate(R.menu.save_menu)

            holder.view.setOnLongClickListener {
                popup.show()
                true
            }

            holder.view.setOnClickListener {
                startGame(Player.fromSave(save))
            }

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.rename -> {
                        pickNameDialog { newName ->
                            save.name = newName
                            notifyItemChanged(holder.adapterPosition)
                        }
                    }
                    R.id.delete -> {
                        var dialog = null as Dialog?
                        dialog = AlertDialog.Builder(holder.view.context)
                                .setTitle("Удалить сохранение?")
                                .setPositiveButton("Удалить") { _, _ ->
                                    SaveLoader.delete(save)
                                    toast("Удалено")
                                    notifyItemRemoved(holder.adapterPosition)
                                }
                                .setNegativeButton("Отмена") { _, _ ->
                                    dialog!!.dismiss()
                                }
                                .show()
                    }
                }

                true
            }
        }

    }

    override fun onPause() {
        SaveLoader.save(filesDir)
        super.onPause()
    }
}
