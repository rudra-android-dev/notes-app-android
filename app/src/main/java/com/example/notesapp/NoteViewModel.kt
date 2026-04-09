package com.example.notesapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val allNotes = MutableLiveData<List<Note>>()

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        loadNotes()
    }

    fun loadNotes() = viewModelScope.launch {
        allNotes.value = repository.getAllNotes()
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
        loadNotes()
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
        loadNotes()
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
        loadNotes()
    }

    fun search(query: String) = viewModelScope.launch {
        if (query.isEmpty()) {
            allNotes.value = repository.getAllNotes()
        } else {
            allNotes.value = repository.searchNotes("%$query%")
        }
    }
}