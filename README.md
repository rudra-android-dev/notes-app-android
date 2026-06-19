# Notes App

A modern Android note-taking app built with Kotlin, following MVVM architecture 
with local data persistence using Room Database.

## Screenshots



## Features
- Create, edit, and delete notes
- Real time search by title or description
- Sort notes A-Z or Z-A
- Swipe left/right to delete with undo (Snackbar)
- Timestamp shown on each note card
- Splash screen on app launch
- Empty state UI when no notes exist
- Smooth RecyclerView layout animation

## Tech Stack
- Kotlin
- MVVM Architecture
- Room Database (v2) with KSP
- ViewModel + LiveData
- RecyclerView with ItemTouchHelper
- Kotlin Coroutines (viewModelScope)
- Material Design 3
- Edge-to-Edge UI

## Architecture
Follows MVVM pattern:

UI (MainActivity / AddNoteActivity)
    ↓
NoteViewModel (AndroidViewModel)
    ↓
NoteRepository
    ↓
NoteDao (Room DAO)
    ↓
NoteDatabase (SQLite via Room)

## Project Structure
com.example.notesapp
├── Note.kt              → Room Entity
├── NoteDao.kt           → Database queries (insert, update, delete, search)
├── NoteDatabase.kt      → Room Database singleton
├── NoteRepository.kt    → Data layer abstraction
├── NoteViewModel.kt     → Business logic, LiveData holder
├── NoteAdapter.kt       → RecyclerView Adapter with ViewHolder
├── MainActivity.kt      → Note list, search, sort, swipe-to-delete
├── AddNoteActivity.kt   → Add and edit notes
└── SplashActivity.kt    → 1.5s splash screen

## Installation
1. Clone the repo
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or physical device (Android 5.0+)

## What I Learned
- Implementing full MVVM architecture from scratch
- Managing local data with Room Database and suspend functions
- Handling Android lifecycle with ViewModel and LiveData
- Using Kotlin Coroutines with viewModelScope for background operations
- Building swipe gestures with ItemTouchHelper

## Challenges Faced
- Understanding how ViewModel survives configuration changes
- Setting up Room with KSP (Kotlin Symbol Processing) instead of KAPT
- Making the save button only enable when fields have content

## Future Improvements
- Note categories / labels
- Dark mode toggle
- Cloud backup
- Note pinning
- Rich text formatting

## License
MIT License
