package ru.rpuxa.bomjara.activities

import android.annotation.SuppressLint
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
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.save.Save
import ru.rpuxa.bomjara.save.SaveLoader
import ru.rpuxa.bomjara.settings.Settings


class SavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val new = intent?.extras?.get("new")

        setContentView(R.layout.activity_save)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)
        val savesAdapter = SavesAdapter(SaveLoader.saves.list)
        recycler.adapter = savesAdapter

        new_save.setOnClickListener {
            pickNameDialog(randName) { name ->
                startGame(Player(random.nextLong(), name, false))
            }
        }
        if (new != null && new is Boolean && new) {
            new_save.performClick()
        }

    }

    private fun pickNameDialog(startName: String, onName: (String) -> Unit) {
        val nameEditText = EditText(this).apply {
            setText(startName)
            hint = "Имя"
            setSelection(text.length)
        }
        val dialog = AlertDialog.Builder(this)
                .setTitle("Введите имя бомжа")
                .setView(nameEditText)
                .setPositiveButton("Ок", null)
                .setNegativeButton("Отмена", null)
                .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = nameEditText.text.toString()
            if (name.length < 4) {
                toast("Имя должно быть не меньше 4 символов")
                return@setOnClickListener
            }
            onName(name)
        }
    }

    private fun startGame(player: Player) {
        Player.CURRENT = player
        if (player.age == 0)
            startActivity<PrehistoryActivity>()
        else {
            startActivity<ContentActivity>()
            Settings.lastSave = player.id
        }
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
                        pickNameDialog(save.name) { newName ->
                            save.name = newName
                            notifyItemChanged(holder.adapterPosition)
                        }
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(holder.view.context)
                                .setTitle("Удалить сохранение?")
                                .setPositiveButton("Удалить") { _, _ ->
                                    SaveLoader.delete(save)
                                    toast("Удалено")
                                    notifyItemRemoved(holder.adapterPosition)
                                }
                                .setNegativeButton("Отмена", null)
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

    companion object {
        val names = arrayOf(
                "Геннадий", "Василий", "Анатолий", "Семён", "Вадим"
        )

        val randName get() = "Бомж ${names[random.nextInt(names.size)]}"
    }
}
