package dz.mhyrdarzi.mhyrnoteapp.data.repository

import dz.mhyrdarzi.mhyrnoteapp.data.database.dao.NoteDao
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: NoteDao) {
    fun getAllNotes() = dao.getAllNotes()
    fun filterNote(priority: String) = dao.filterNote(priority)
    fun searchNote(title: String) = dao.searchNote(title)
    suspend fun deleteNote(model: NoteModel) = dao.deleteNote(model)
}