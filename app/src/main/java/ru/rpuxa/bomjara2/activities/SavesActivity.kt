package ru.rpuxa.bomjara2.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_save.*
import kotlinx.android.synthetic.main.save_card_view.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.save.SaveInfo
import ru.rpuxa.bomjara2.toast


class SavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)
        val savesAdapter = SavesAdapter(listOf(
                SaveInfo("123", 123, 1, 89125123),
                SaveInfo("123", 123, 1, 89123),
                SaveInfo("123", 123, 1, 912523),
                SaveInfo("123", 123, 1, 825123),
                SaveInfo("123", 123, 1, 8123)
        ))
        recycler.adapter = savesAdapter

        new_save.setOnClickListener {
            var dialog = null as Dialog?
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Введите имя бомжа")

            val nameEditText = EditText(this)
            builder.setView(nameEditText)

            builder.setPositiveButton("Создать") { _, _ ->
                val name = nameEditText.text.toString()
                if (name.length < 4) {
                    toast("Имя должно быть не меньше 4 символов")
                    return@setPositiveButton
                }
                dialog!!.dismiss()
                startGame(Player(name, false))
            }

            builder.setNegativeButton("Отмена") { _, _ ->
                dialog!!.dismiss()
            }

            dialog = builder.show()
        }

    }

    private fun startGame(player: Player) {
        Player.CURRENT = player
        startActivity(Intent(this, ContentActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SavesAdapter(private val list: List<SaveInfo>) : RecyclerView.Adapter<SavesAdapter.SaveViewHolder>() {

        class SaveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val saveName = view.name!!
            val age = view.age!!
            val money = view.money!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.save_card_view, parent, false)
            view.delete.setOnClickListener {

            }
            view.delete.visibility = View.GONE

            return SaveViewHolder(view)
        }

        override fun getItemCount() = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
            val save = list[position]
            holder.saveName.text = save.name
            holder.age.text = "${save.ageYear} лет ${save.ageDays} дней"
            holder.money.text = save.money.toString()


        }

    }

    companion object {
        val names = arrayOf(
                ""
        )
    }
}
