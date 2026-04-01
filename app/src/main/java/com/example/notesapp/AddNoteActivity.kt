package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val titleEdit = findViewById<EditText>(R.id.editTitle)
        val descEdit = findViewById<EditText>(R.id.editDescription)
        val saveBtn = findViewById<Button>(R.id.btnSave)

        saveBtn.setOnClickListener {
            val title = titleEdit.text.toString()
            val description = descEdit.text.toString()

            val intent = Intent()
            intent.putExtra("title", title)
            intent.putExtra("description", description)

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}