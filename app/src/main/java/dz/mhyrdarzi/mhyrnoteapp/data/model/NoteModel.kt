package dz.mhyrdarzi.mhyrnoteapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants

@Entity(tableName = Constants.NOTE_MODEL)
data class NoteModel (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var title:String = "",
    var desc:String = "",
    var category:String = "",
    var priority:String = ""
)