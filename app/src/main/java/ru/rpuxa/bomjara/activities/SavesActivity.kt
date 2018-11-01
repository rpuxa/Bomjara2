package ru.rpuxa.bomjara.activities

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
import ru.rpuxa.bomjara.settings.settings


class SavesActivity : AppCompatActivity() {

    private lateinit var names: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val new = intent?.extras?.get("new")
        names = resources.getStringArray(R.array.homeless_names)

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

    private fun startGame(player: Player) {
        Player.current = player
        if (player.age == 0)
            startActivity<PrehistoryActivity>()
        else {
            startActivity<ContentActivity>()
            settings.lastSave = player.id
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_in, R.anim.rigth_out)
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
                            toast(getString(R.string.renamed))
                        }
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(holder.view.context)
                                .setTitle(getString(R.string.delete_save))
                                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                                    SaveLoader.delete(save)
                                    toast(getString(R.string.deleted))
                                    notifyItemRemoved(holder.adapterPosition)
                                }
                                .setNegativeButton(getString(R.string.cancel), null)
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

    private val randName get() = "Бомж " + names[random.nextInt(names.size)]
}
