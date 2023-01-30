package dz.mhyrdarzi.mhyrnoteapp.data.repository

import dz.mhyrdarzi.mhyrnoteapp.data.database.dao.NoteDao
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dao: NoteDao) {
    suspend fun saveNote(model: NoteModel) = dao.insertNote(model)
    suspend fun updateNote(model: NoteModel) = dao.updateNote(model)
    fun getNote(id: Int) = dao.getNote(id)
}