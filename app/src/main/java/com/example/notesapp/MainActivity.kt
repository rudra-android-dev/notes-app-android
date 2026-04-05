package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val EXTRA_TITLE = "extra_title"
const val EXTRA_DESCRIPTION = "extra_description"
const val EXTRA_POSITION = "extra_position"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var viewModel: NoteViewModel

    private val addNoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {

            val data = result.data
            val title = data?.getStringExtra(EXTRA_TITLE)
            val description = data?.getStringExtra(EXTRA_DESCRIPTION)
            val position = data?.getIntExtra(EXTRA_POSITION, -1)

            if (title != null && description != null) {

                // 🔹 UPDATE
                if (position != null && position != -1) {
                    val updatedNote = Note(
                        id = noteList[position].id,
                        title = title,
                        description = description
                    )
                    viewModel.update(updatedNote)
                }

                // 🔹 INSERT
                else {
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

        // ✅ ViewModel init (correct place)
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

        // ✅ OBSERVE DATA (this replaces ALL manual updates)
        viewModel.allNotes.observe(this) { notes ->
            noteList.clear()
            noteList.addAll(notes)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    // 🔹 DELETE
    private fun deleteNote(position: Int) {
        val note = noteList[position]
        viewModel.delete(note)

        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
    }
}