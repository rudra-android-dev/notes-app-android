package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val noteList: ArrayList<Note>,
    private val onItemClick: (position: Int) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val accentColors = listOf(
        R.color.note_accent_1,
        R.color.note_accent_2,
        R.color.note_accent_3,
        R.color.note_accent_4,
        R.color.note_accent_5
    )

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val description: TextView = itemView.findViewById(R.id.noteDescription)
        val time: TextView = itemView.findViewById(R.id.noteTime)
        val colorIndicator: View = itemView.findViewById(R.id.colorIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note.title
        holder.description.text = note.description

        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        holder.time.text = sdf.format(Date(note.timestamp))

        // Cycle accent colours, each card gets a different colour in order
        val colorRes = accentColors[position % accentColors.size]
        holder.colorIndicator.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, colorRes)
        )

        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount() = noteList.size
}