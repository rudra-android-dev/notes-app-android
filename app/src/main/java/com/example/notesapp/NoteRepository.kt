package com.example.notesapp

class NoteRepository(private val dao: NoteDao) {

    suspend fun insert(note: Note) = dao.insert(note)

    suspend fun update(note: Note) = dao.update(note)

    suspend fun delete(note: Note) = dao.delete(note)

    suspend fun getAllNotes(): List<Note> = dao.getAllNotes()
}