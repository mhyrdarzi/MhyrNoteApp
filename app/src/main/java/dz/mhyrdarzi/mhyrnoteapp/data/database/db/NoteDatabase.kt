package dz.mhyrdarzi.mhyrnoteapp.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dz.mhyrdarzi.mhyrnoteapp.data.database.dao.NoteDao
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel

@Database(entities = [NoteModel::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao() : NoteDao
}