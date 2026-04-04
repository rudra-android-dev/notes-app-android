package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

const val EXTRA_TITLE = "extra_title"
const val EXTRA_DESCRIPTION = "extra_description"
const val EXTRA_POSITION = "extra_position"

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var dao: NoteDao

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
                    lifecycleScope.launch {
                        val updatedNote = Note(
                            id = noteList[position].id,
                            title = title,
                            description = description
                        )

                        dao.update(updatedNote)

                        noteList[position] = updatedNote
                        recyclerView.adapter?.notifyItemChanged(position)
                    }
                } else {
                    lifecycleScope.launch {
                        val newNote = Note(title = title, description = description)
                        dao.insert(newNote)

                        noteList.add(newNote)
                        recyclerView.adapter?.notifyItemInserted(noteList.size - 1)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        noteList = ArrayList()

        noteList.add(Note(title = "Shopping", description = "Buy milk and eggs"))
        noteList.add(Note(title = "Workout", description = "Go to gym at 6 PM"))
        noteList.add(Note(title = "Study", description = "Revise Android basics"))
        noteList.add(Note(title = "Meeting", description = "Project discussion at 3 PM"))
        noteList.add(Note(title = "Call", description = "Call friend tonight"))

        val adapter = NoteAdapter(
            noteList,
            onItemClick = { position ->
                val note = noteList[position]

                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra(EXTRA_TITLE, note.title)
                intent.putExtra(EXTRA_DESCRIPTION, note.description)
                intent.putExtra(EXTRA_POSITION, position)

                addNoteLauncher.launch(intent)
            },
            onItemLongClick = { position ->
                deleteNote(position)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            addNoteLauncher.launch(intent)
        }
        val db = NoteDatabase.getDatabase(this)
        dao = db.noteDao()

        lifecycleScope.launch {
            val notes = dao.getAllNotes()
            noteList.clear()
            noteList.addAll(notes)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }
    private fun deleteNote(position: Int) {
        val note = noteList[position]

        lifecycleScope.launch {
            dao.delete(note)

            noteList.removeAt(position)
            recyclerView.adapter?.notifyItemRemoved(position)

            Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}