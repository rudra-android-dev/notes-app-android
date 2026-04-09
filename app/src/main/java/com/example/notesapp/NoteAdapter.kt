package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val noteList: ArrayList<Note>,
    private val onItemClick: (position: Int) -> Unit,
    private val onItemLongClick: (position: Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val description: TextView = itemView.findViewById(R.id.noteDescription)
        val time: TextView = itemView.findViewById(R.id.noteTime)
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

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}