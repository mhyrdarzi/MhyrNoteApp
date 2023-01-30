package dz.mhyrdarzi.mhyrnoteapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.NOTE_MODEL
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(model: NoteModel)

    @Delete
    suspend fun deleteNote(model: NoteModel)

    @Update
    suspend fun updateNote(model: NoteModel)

    @Query("select * from $NOTE_MODEL")
    fun getAllNotes() : Flow<MutableList<NoteModel>>

    @Query("select * from $NOTE_MODEL where id == :id")
    fun getNote(id: Int) : Flow<NoteModel>

    @Query("select * from $NOTE_MODEL where priority == :priority")
    fun filterNote(priority: String) : Flow<MutableList<NoteModel>>

    @Query("select * from $NOTE_MODEL where title like :title || '%' ")
    fun searchNote(title: String): Flow<MutableList<NoteModel>>
}