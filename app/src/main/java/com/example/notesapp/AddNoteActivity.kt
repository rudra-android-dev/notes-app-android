package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.EditText

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
        val saveBtn = findViewById<MaterialButton>(R.id.btnSave)
        val backBtn = findViewById<ImageButton>(R.id.btnBack)

        saveBtn.isEnabled = false

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val titleText = titleEdit.text.toString().trim()
                val descriptionText = descEdit.text.toString().trim()
                saveBtn.isEnabled = titleText.isNotEmpty() || descriptionText.isNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        titleEdit.addTextChangedListener(watcher)
        descEdit.addTextChangedListener(watcher)

        val receivedTitle = intent.getStringExtra(EXTRA_TITLE)
        val receivedDescription = intent.getStringExtra(EXTRA_DESCRIPTION)
        if (receivedTitle != null && receivedDescription != null) {
            titleEdit.setText(receivedTitle)
            descEdit.setText(receivedDescription)
        }

        val position = intent.getIntExtra(EXTRA_POSITION, -1)

        backBtn.setOnClickListener {
            finish()
            applyTransitionClose()
        }

        saveBtn.setOnClickListener {
            val title = titleEdit.text.toString()
            val description = descEdit.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_TITLE, title)
            resultIntent.putExtra(EXTRA_DESCRIPTION, description)
            resultIntent.putExtra(EXTRA_POSITION, position)

            setResult(RESULT_OK, resultIntent)
            finish()
            applyTransitionClose()
        }
    }

    private fun applyTransitionClose() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}