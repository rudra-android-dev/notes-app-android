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

        val receivedTitle = intent.getStringExtra(EXTRA_TITLE)
        val receivedDescription = intent.getStringExtra(EXTRA_DESCRIPTION)

        if (receivedTitle != null && receivedDescription != null) {
            titleEdit.setText(receivedTitle)
            descEdit.setText(receivedDescription)
        }

        val position = intent.getIntExtra(EXTRA_POSITION, -1)
        val isEdit = position != -1

        saveBtn.setOnClickListener {
            val title = titleEdit.text.toString()
            val description = descEdit.text.toString()

            val intent = Intent()
            intent.putExtra(EXTRA_TITLE,title)
            intent.putExtra(EXTRA_DESCRIPTION,description)
            intent.putExtra(EXTRA_POSITION, position)

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}