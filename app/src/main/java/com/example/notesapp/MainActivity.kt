package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.animation.AnimationUtils
import com.google.android.material.snackbar.Snackbar

const val EXTRA_TITLE = "extra_title"
const val EXTRA_DESCRIPTION = "extra_description"
const val EXTRA_POSITION = "extra_position"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var viewModel: NoteViewModel

    private lateinit var searchEditText: EditText
    private var fullList: List<Note> = listOf()

    private lateinit var emptyText: TextView

    private val addNoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {

            val data = result.data
            val title = data?.getStringExtra(EXTRA_TITLE)
            val description = data?.getStringExtra(EXTRA_DESCRIPTION)
            val position = data?.getIntExtra(EXTRA_POSITION, -1)

            if (title != null && description != null) {

                if (position != null && position != -1) {
                    val updatedNote = Note(
                        id = noteList[position].id,
                        title = title,
                        description = description
                    )
                    viewModel.update(updatedNote)
                } else {
                    val newNote = Note(title = title, description = description)
                    viewModel.insert(newNote)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        emptyText = findViewById(R.id.emptyText)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        noteList = ArrayList()

        val adapter = NoteAdapter(
            noteList,
            onItemClick = { position ->
                val note = noteList[position]

                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra(EXTRA_TITLE, note.title)
                intent.putExtra(EXTRA_DESCRIPTION, note.description)
                intent.putExtra(EXTRA_POSITION, position)

                addNoteLauncher.launch(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            },
            onItemLongClick = { position ->
                deleteNote(position)
            }
        )

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedNote = noteList[position]

                    viewModel.delete(deletedNote)

                    Snackbar.make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO") {
                            viewModel.insert(deletedNote)
                        }
                        .show()
                }
            }
        )

        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)

        fab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            addNoteLauncher.launch(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        viewModel.allNotes.observe(this) { notes ->
            fullList = notes

            noteList.clear()
            noteList.addAll(notes)
            adapter.notifyDataSetChanged()

            recyclerView.scheduleLayoutAnimation()

            emptyText.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        searchEditText = findViewById(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun deleteNote(position: Int) {
        val note = noteList[position]
        viewModel.delete(note)
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
    }

    private fun filterNotes(query: String) {
        val filteredList = fullList.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }

        noteList.clear()
        noteList.addAll(filteredList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_az -> {
                noteList.sortBy { it.title }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            R.id.sort_za -> {
                noteList.sortByDescending { it.title }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        return true
    }
}